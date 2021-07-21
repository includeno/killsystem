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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional //开启事务
@Slf4j
@Service
public class KillServiceImpl implements KillService {
    @Autowired
    Gson gson;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    KafkaProducerService kafkaProducerService;

    @Override
    public ItemKillSuccess kill(Integer killId, Integer userId) {
        //秒杀id->秒杀商品key->秒杀商品
        String itemKillKey= RedisKeyUtil.getItemKillKey(killId);
        String itemKillString= (String) redisTemplate.opsForValue().get(itemKillKey);
        log.warn("itemKill :"+itemKillString);
        ItemKill itemKill=gson.fromJson(itemKillString,ItemKill.class);
        Integer itemId=itemKill.getItemId();
        //异步扣库存
        ItemKillSuccess map = updateStock_CAS_Redis(userId, killId, itemId);

        return map;
    }

    //扣除库存方案2：redis扣除库存并下单
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
}
