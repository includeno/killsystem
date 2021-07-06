package com.cloudcommons.service.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcommons.entity.Item;
import com.cloudcommons.entity.ItemKill;
import com.cloudcommons.entity.ItemKillSuccess;
import com.cloudcommons.entity.User;
import com.cloudcommons.exception.CustomException;
import com.cloudcommons.mapper.ItemKillMapper;
import com.cloudcommons.mapper.ItemKillSuccessMapper;
import com.cloudcommons.mapper.ItemMapper;
import com.cloudcommons.mapper.UserMapper;
import com.cloudcommons.service.ItemKillSuccessService;
import com.cloudcommons.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//import com.cloudcommons.event.EventProducer;

/**
 * <p>
 * 秒杀成功订单表 服务实现类
 * </p>
 */
@Service
@Transactional //开启事务
@Slf4j
public class ItemKillSuccessServiceImpl extends ServiceImpl<ItemKillSuccessMapper, ItemKillSuccess> implements ItemKillSuccessService {

    @Autowired
    private ItemKillMapper itemKillMapper;
    @Autowired
    private ItemKillSuccessMapper iksMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    //@Autowired
    //private EventProducer producer;

    //秒杀业务，仅用于测试
    @Override
    public Map<String, Object> kill(Integer killId, Integer userId) {
        String itemKillKey=RedisKeyUtil.getItemKillKey(killId);

        ItemKill itemKill=(ItemKill)redisTemplate.opsForValue().get(itemKillKey);
        Integer itemId=itemKill.getItemId();

        Map<String, Object> map = updateStock_CAS_Redis(userId, killId, itemId);
        System.out.println("生成订单c："+map);
        //消息队列发送事件 即生成订单
        //return eventService.createOrderEvent(itemKill.getItemId(),killId,userId);
        return map;
    }

    //扣除库存方案2：redis扣除库存并下单
    private Map<String, Object> updateStock_CAS_Redis(Integer userId, Integer killId, Integer itemId){

        String killStockKey = RedisKeyUtil.getKillStockKey(itemId,killId);
        System.out.println("killStockKey:"+killStockKey);
        String uuid = (String) redisTemplate.opsForList().leftPop(killStockKey);
        System.out.println("uuid:"+uuid);
        if (uuid == null || uuid.isEmpty()) {
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"该商品已售空");
        }else {
            //秒杀成功则秒杀成功的用户放入list中
            Map<String,Object> map=new HashMap<>();
            map.put("userId",userId);
            map.put("killId",killId);
            map.put("itemId",itemId);
            map.put("uuid",uuid);
            map.put("pay_status",0);//0未支付，1已支付
            redisTemplate.opsForHash().put(RedisKeyUtil.getKillSuccessKey(userId),uuid,map);
            //producer.send(map);
            return map;
        }
    }

    //加盐值的秒杀业务
    @Override
    public Map<String, Object> kill2(Integer killId, Integer userId, String md5) {
        Integer itemId=8;
        Map<String, Object> map = updateStock_CAS_Redis2(userId, killId, itemId, md5);
        System.out.println("生成订单code："+map);
        return map;
    }

    //扣除库存方案2：加盐值的redis扣除库存并异步下单
    private Map<String, Object> updateStock_CAS_Redis2(Integer userId, Integer killId, Integer itemId, String md5){

        if (redisTemplate.hasKey(RedisKeyUtil.getKillTimeKey(itemId))) {
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"当前商品的抢购活动已经结束了~");
        }
        log.info("用户信息{},对应的md5{}",userId,md5);
        //验证是否是同一个用户(该用户未盗用其他用户的md5)
        if (!md5.equals(md5(userId))){
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"请重新尝试！");
        }
        //限制单用户频繁抢购 超过五次则稍后重试
        String md5UserKey = RedisKeyUtil.getMd5UserKey(md5);
        Integer count = (Integer) redisTemplate.opsForValue().get(md5UserKey);
        if (count==null){
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"请重新尝试！");
        }
        //查询单用户访问频率过高
        if (count>=5){
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"操作过于频繁请稍后再试");
        }else {
            redisTemplate.opsForValue().increment(md5UserKey);
        }
        //TODO 查询该用户是否已经抢购 待优化不能查库，影响性能，改为token校验
        ItemKillSuccess iks = iksMapper.selectOne(new QueryWrapper<ItemKillSuccess>()
                .eq("user_id", userId)
                .eq("kill_id", killId));
        if (iks!=null){
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"您已抢购过该商品");
        }
        //取库存（list）
        String killStockKey = RedisKeyUtil.getKillStockKey(itemId,killId);
        String uuid = (String)redisTemplate.opsForList().leftPop(killStockKey);
        if (uuid == null && uuid.length() == 0) {
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"该商品已售空");
        }else {
            //秒杀成功则秒杀成功的用户放入redis中
            Map<String,Object> map=new HashMap<>();
            map.put("userId",userId);
            map.put("killId",killId);
            map.put("itemId",itemId);
            map.put("uuid",uuid);
            map.put("pay_status",0);//0未支付，1已支付
            //通过uuid来记录订单
            //每个用户对应多个订单，每个订单都是uuid作为Key，订单内容作为Value
            redisTemplate.opsForHash().put(RedisKeyUtil.getKillSuccessKey(userId),uuid,map);
            //发送订单消息到消息队列
            //producer.send(map);
            return map;
        }
    }

    //接口加密（通用方法）每个用户秒杀商品连接都会生成md5
    @Override
    public String getMd5(Integer killId, Integer userId) {
        //检验用户的合法性  可以省略，由于访问该方法需要用户凭证，所以相当于对用户进行了身份校验
        User user = userMapper.selectById(userId);
        if (user==null){
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"用户信息不存在！");
        }
        log.info("用户信息:[{}]",user.toString());
        //校验秒杀活动合法性
        ItemKill itemKill=findItemKill(killId);
        if (itemKill==null){
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"商品信息不合法");
        }
        //检验商品合法性
        Item item = findItem(itemKill.getItemId());
        if (item==null){
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"商品信息不合法");
        }
        log.info("商品信息:[{}]",item.toString());
        //生成hashKey, md5可以加盐值
        String md5Key= md5(userId);
        String key=RedisKeyUtil.getMd5UserKey(md5Key);
        redisTemplate.opsForValue().set(key,new Integer(0),5, TimeUnit.SECONDS);
        log.info("Redis写入:[{}] [{}]",userId ,key);
        return md5Key;
    }

    @Override
    public IPage<ItemKillSuccess> findList(IPage<ItemKillSuccess> page)
    {
        QueryWrapper<ItemKillSuccess> wrapper = new QueryWrapper<>();
        IPage<ItemKillSuccess> result=iksMapper.selectPage(page,wrapper);
        return result;
    }

    //md5加密算法
    private String md5(Integer userId){
        return SecureUtil.md5(String.valueOf(userId));
    }

    //查找商品信息（带缓存）
    private Item findItem(Integer itemId) {
        String itemKey = RedisKeyUtil.getItemKey(itemId);
        Item item = (Item) redisTemplate.opsForValue().get(itemKey);
        if (item==null){
            //若缓存中没有则从数据库中查询
            item=itemMapper.selectById(itemId);
            //写回缓存
            redisTemplate.opsForValue().set(itemKey,item);
        }
        return item;
    }

    //查找秒杀活动信息（带缓存）
    private ItemKill findItemKill(Integer killId) {
        String itemKillKey = RedisKeyUtil.getItemKillKey(killId);
        ItemKill itemKill = (ItemKill) redisTemplate.opsForValue().get(itemKillKey);
        if (itemKill==null){
            //若缓存中没有则从数据库中查询
            itemKill=itemKillMapper.selectById(killId);
            //写回缓存
            redisTemplate.opsForValue().set(itemKillKey,itemKill);
        }
        return itemKill;
    }

    //校验库存(mysql版)
    private ItemKill checkStock(Integer killId){
        //根据商品id查询库存
        ItemKill itemKill = itemKillMapper.selectById(killId);
        if (itemKill.getTotal()<=0){
            throw new RuntimeException("库存不足！！！");
        }
        return itemKill;
    }

    //扣除库存方案1(mysql版) 采用mysql乐观锁判断，若失败则抛出异常，若成功则更新
    private int updateStock(ItemKill itemKill){
        //乐观锁
        itemKill.setTotal(itemKill.getTotal()-1);
        int i = itemKillMapper.updateById(itemKill);
        if (i==0){
            throw new RuntimeException("请重试！！");
        }
        return i;
    }

    //创建秒杀订单(mysql版)（雪花id）
    //TODO 改为发送消息队列的方式创建订单 ✔
    private String createIksOrder(Integer itemId, Integer killId){
        //生成订单
        ItemKillSuccess iksOrder=new ItemKillSuccess();
        //生成雪花id
        SnowflakeGenerator snowFlake=new SnowflakeGenerator();
        iksOrder.setId(String.valueOf(snowFlake.next()))
                .setItemId(itemId).setUserId(10).setKillId(killId);
        baseMapper.insert(iksOrder);
        return iksOrder.getId();
    }
}
