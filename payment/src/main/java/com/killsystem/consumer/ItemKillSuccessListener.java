package com.killsystem.consumer;

import com.killsystem.config.KafkaTopic;
import com.killsystem.entity.ItemKill;
import com.killsystem.entity.ItemKillSuccess;
import com.killsystem.service.ItemKillService;
import com.killsystem.service.ItemKillSuccessService;
import com.killsystem.utils.RedisKeyUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;

//根据传入的数据类型自动划分
@Slf4j
@Configuration
@EnableKafka
@KafkaListener(id = "itemKillSuccess", topics = KafkaTopic.itemKillSuccess)
public class ItemKillSuccessListener {
    @Autowired
    Gson gson;

    @Autowired
    ItemKillSuccessService itemKillSuccessService;

    @Autowired
    ItemKillService itemKillService;

    @Autowired
    private RedisTemplate redisTemplate;

    @KafkaHandler
    public void listen(String message) {
        ItemKillSuccess itemKillSuccess=gson.fromJson(message,ItemKillSuccess.class);//String->ItemKillSuccess
        log.warn("kafka consumer "+(message));

        //检查redis内订单
        String order = (String) redisTemplate.opsForHash().get(RedisKeyUtil.getKillSuccessKey(itemKillSuccess.getUserId()), itemKillSuccess.getId());
        if(order==null){
            log.error("redis内订单Key不存在");
            return;
        }
        ItemKillSuccess temp=gson.fromJson(order,ItemKillSuccess.class);
        if(temp==null){
            log.error("redis内订单ItemKillSuccess不存在");
            return;
        }

        //检查库存
        checkStock(itemKillSuccess.getItemId());
        //订单状态更新
        itemKillSuccess.setStatus(ItemKillSuccess.Status.PAID.getCode());
        //MySql中插入订单
        itemKillSuccessService.save(itemKillSuccess);

        //发送延时消息检查订单状态
        //https://www.confluent.io/blog/apache-kafka-purgatory-hierarchical-timing-wheels/ timingwheel
        //https://github.com/ricall/kafka-delay-service
        //https://medium.com/naukri-engineering/retry-mechanism-and-delay-queues-in-apache-kafka-528a6524f722
        //https://kafka.apache.org/26/javadoc/index.html kafka javadoc
        //TimingWheel timingWheel;

    }

    //校验库存(mysql版)
    private ItemKill checkStock(Integer killId){
        //根据商品id查询库存
        ItemKill itemKill = itemKillService.getById(killId);
        if (itemKill.getTotal()<=0){
            throw new RuntimeException("库存不足！！！");
        }
        return itemKill;
    }
}
