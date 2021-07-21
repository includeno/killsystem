package com.killsystem.service.impl;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.killsystem.entity.Item;
import com.killsystem.entity.ItemKill;
import com.killsystem.entity.ItemKillSuccess;
import com.killsystem.mapper.ItemKillMapper;
import com.killsystem.mapper.ItemKillSuccessMapper;
import com.killsystem.mapper.ItemMapper;
import com.killsystem.mapper.UserMapper;
import com.killsystem.service.ItemKillSuccessService;
import com.killsystem.utils.RedisKeyUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.cloudcommons.event.EventProducer;

/**
 * <p>
 * 秒杀成功订单表 服务实现类
 * </p>
 */

@Transactional //开启事务
@Slf4j
@Service
public class ItemKillSuccessServiceImpl extends ServiceImpl<ItemKillSuccessMapper, ItemKillSuccess> implements ItemKillSuccessService {

    @Autowired
    Gson gson;
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

    @Override
    public IPage<ItemKillSuccess> findList(IPage<ItemKillSuccess> page)
    {
        QueryWrapper<ItemKillSuccess> wrapper = new QueryWrapper<>();
        IPage<ItemKillSuccess> result=iksMapper.selectPage(page,wrapper);
        return result;
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
