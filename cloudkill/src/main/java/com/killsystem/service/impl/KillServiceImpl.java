package com.killsystem.service.impl;

import cn.hutool.http.HttpStatus;
import com.killsystem.entity.ItemKill;
import com.killsystem.entity.ItemKillSuccess;
import com.killsystem.exception.CustomException;
import com.killsystem.service.KafkaProducerService;
import com.killsystem.service.KillService;
import com.killsystem.utils.RedisKeyUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Transactional //开启事务
@Service
public class KillServiceImpl implements KillService {
    public Logger log= LoggerFactory.getLogger("KillServiceImpl");
    @Autowired
    Gson gson;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    KafkaProducerService kafkaProducerService;

    @Override
    public ItemKillSuccess kill(Integer killId, Integer userId) {
        //秒杀id->秒杀商品key->秒杀商品
        String killTimeKey = RedisKeyUtil.getKillTimeKey(killId);
        Object killTimeValue = redisTemplate.opsForValue().get(killTimeKey);
        //活动已结束 redis内没有秒杀活动时间key
        if(killTimeValue==null){
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"秒杀活动已结束");
        }

        //根据秒杀活动查询秒杀商品id
        String itemKillString= (String) redisTemplate.opsForValue().get(RedisKeyUtil.getItemKillKey(killId));
        if(itemKillString==null){
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"秒杀活动已结束 无法查询到秒杀商品");
        }
        log.warn("itemKill :"+itemKillString);
        ItemKill itemKill=gson.fromJson(itemKillString,ItemKill.class);

        Integer itemId=itemKill.getItemId();
        //异步扣库存
        ItemKillSuccess map = updateStock_CAS_Redis(userId, killId, itemId);

        return map;
    }

    @Override
    public ItemKillSuccess killByMysql(Integer killId, Integer userId){

        //检查秒杀活动
        String killTimeKey = RedisKeyUtil.getKillTimeKey(killId);
        Object killTimeValue = redisTemplate.opsForValue().get(killTimeKey);
        //活动已结束 redis内没有秒杀活动时间key
        if(killTimeValue==null){
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"秒杀活动已结束");
        }

        //检查秒杀商品
        //根据秒杀活动查询秒杀商品id
        String itemKillString= (String) redisTemplate.opsForValue().get(RedisKeyUtil.getItemKillKey(killId));
        log.warn("itemKill :"+itemKillString);
        if(itemKillString==null){
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"秒杀活动已结束 无法查询到秒杀商品");
        }
        ItemKill itemKill=gson.fromJson(itemKillString,ItemKill.class);
        Integer itemId=itemKill.getItemId();

        return updateStock_CAS_Mysql(userId, killId, itemId);

    }

    //扣除库存方案：redis扣除库存并下单
    private ItemKillSuccess updateStock_CAS_Redis(Integer userId, Integer killId, Integer itemId){
        //根据秒杀商品和活动 获取库存hashkey
        String killStockKey = RedisKeyUtil.getKillStockKey(itemId,killId);
        //获取库存具体key
        String uuid = (String) redisTemplate.opsForList().leftPop(killStockKey);

        if (uuid == null || uuid.isEmpty()) {
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"该商品已售空");
        }else {
            //发送添加ItemKillSuccess的消息
            ItemKillSuccess itemKillSuccess=new ItemKillSuccess();
            itemKillSuccess.setKillId(killId);
            itemKillSuccess.setItemId(itemId);
            itemKillSuccess.setUserId(userId);
            itemKillSuccess.setId(uuid);//以库存id为订单id
            Date now=new Date();
            itemKillSuccess.setCreateTime(now);
            itemKillSuccess.setUpdateTime(now);
            itemKillSuccess.setStatus(ItemKillSuccess.Status.NOTPAY.getCode());

            //redis 内生成订单
            redisTemplate.opsForHash().put(RedisKeyUtil.getKillSuccessKey(userId),uuid,gson.toJson(itemKillSuccess,ItemKillSuccess.class));

            //消息队列异步发送订单消息
            kafkaProducerService.sendAsync(itemKillSuccess);
            return itemKillSuccess;
        }
    }


    private ItemKillSuccess updateStock_CAS_Mysql(Integer userId, Integer killId, Integer itemId){
        //根据秒杀商品和活动 获取Mysql库存

        //https://zhuanlan.zhihu.com/p/266207327
        //https://gitee.com/52itstyle/spring-boot-seckill
        //https://www.iteye.com/blog/1960370817-2517384
        //lua脚本
        String script = " local resultFlag = \"0\" \n" +
                "local key = KEYS[1] \n" +
                "local total = tonumber(redis.call(\"GET\",key)) \n" +
                "if not total then \n" +
                "    return -1 \n" +
                "end \n" +
                "if total >= 1  then \n" +
                "    local ret = redis.call(\"SET\",key,tostring(total-1)) \n" +
                "    return tostring(ret) \n" +
                "end \n" +
                "return -2" ;
        DefaultRedisScript<String>  defaultRedisScript = new DefaultRedisScript<>(script,String.class);

//        List<String> keys = Arrays.asList(killStockKey);
//        String value = "";

        //Redis lua原子操作扣库存
        //Object execute = redisTemplate.execute(defaultRedisScript, keys, value);

        //根据秒杀商品和活动 获取库存hashkey
        String killStockKey = RedisKeyUtil.getKillStockKey(itemId,killId);
        //获取库存具体key
        String uuid = (String) redisTemplate.opsForList().leftPop(killStockKey);

        if (uuid == null || uuid.isEmpty()) {
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"该商品已售空");
        }else {

            //发送添加ItemKillSuccess的消息
            ItemKillSuccess itemKillSuccess=new ItemKillSuccess();
            itemKillSuccess.setKillId(killId);
            itemKillSuccess.setItemId(itemId);
            itemKillSuccess.setUserId(userId);
            itemKillSuccess.setId(UUID.randomUUID().toString());//生成订单id
            Date now=new Date();
            itemKillSuccess.setCreateTime(now);
            itemKillSuccess.setUpdateTime(now);
            itemKillSuccess.setStatus(ItemKillSuccess.Status.NOTPAY.getCode());

            //redis 内生成订单
            redisTemplate.opsForHash().put(RedisKeyUtil.getKillSuccessKey(userId),uuid,gson.toJson(itemKillSuccess,ItemKillSuccess.class));

            //消息队列异步发送订单消息
            kafkaProducerService.sendAsync(itemKillSuccess);
            return itemKillSuccess;
        }


    }

    public Boolean examUser(Integer userId){
        String userRestrictedKey = RedisKeyUtil.getUserRestrictedKey(userId);
        Object userRestricted = redisTemplate.opsForValue().get(userRestrictedKey);

        if(userRestricted==null){
            //限制10+2秒不能抢购
            redisTemplate.opsForValue().set(userRestrictedKey,"yes",new Random().nextInt(2)+10, TimeUnit.SECONDS);
        }
        else{
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"登录太频繁请稍后重试");
        }

        return Boolean.TRUE;

    }

    //在指定毫秒内的用户登录次数 没有进入到秒杀令牌环
    public Boolean examUserInPeriod(Integer userId,Integer period,TimeUnit unit,Integer max){
        String userVisitedInPeriodKey = RedisKeyUtil.getUserVisitedInPeriodKey(userId,period);
        String userVisitedInPeriodValue = (String) redisTemplate.opsForValue().get(userVisitedInPeriodKey);
        
        //统计period时间段内的访问次数
        if(userVisitedInPeriodValue==null){
            //初始化
            redisTemplate.opsForValue().set(userVisitedInPeriodKey,"0",period, unit);
        }
        else{
            Integer userVisitedInPeriodCount=Integer.parseInt(userVisitedInPeriodValue);
            redisTemplate.opsForValue().set(userVisitedInPeriodKey,String.valueOf(userVisitedInPeriodCount+1),period, unit);
            if(userVisitedInPeriodCount>=max){
                throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"指定时间段内登录太频繁 请稍后重试");
            }
        }
        return Boolean.TRUE;
    }
}
