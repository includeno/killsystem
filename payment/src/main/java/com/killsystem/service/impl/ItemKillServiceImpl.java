package com.killsystem.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.killsystem.entity.Item;
import com.killsystem.entity.ItemKill;
import com.killsystem.exception.CustomException;
import com.killsystem.mapper.ItemKillMapper;
import com.killsystem.mapper.ItemMapper;
import com.killsystem.service.ItemKillService;
import com.killsystem.utils.RedisKeyUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 待秒杀商品表 服务实现类
 * </p>
 *
 */

@Transactional //开启事务
@Slf4j
@Service
public class ItemKillServiceImpl extends ServiceImpl<ItemKillMapper, ItemKill> implements ItemKillService {
    @Autowired
    Gson gson;
    @Autowired
    private ItemKillMapper itemKillMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public ItemKill createKill(ItemKill itemKill) {
        //检验合法性
        Item item = itemMapper.selectById(itemKill.getItemId());
        if (item==null){
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"商品id不存在");
        }
        Date now = new Date();

        if (itemKill.getEndTime().getTime() < (now.getTime())){
            throw new CustomException(HttpStatus.HTTP_BAD_REQUEST,"结束时间不合法");
        }
        itemKillMapper.insert(itemKill);//自动添加了主键id

        //redis 添加库存 list
        int total=itemKill.getTotal();
        String killStockKey = RedisKeyUtil.getKillStockKey(itemKill.getItemId(),itemKill.getId());
        for (int i=0;i<total;i++){
            //存入total个随机数，每一个成功订单取出一个随机数
            redisTemplate.opsForList().rightPush(killStockKey, RandomUtil.randomString(32));
        }

        //redis 添加秒杀活动时间
        String killTimeKey = RedisKeyUtil.getKillTimeKey(itemKill.getId());
        redisTemplate.opsForValue().set(killTimeKey,itemKill.getId(),itemKill.getEndTime().getTime()-now.getTime(), TimeUnit.MILLISECONDS);

        //redis 添加秒杀活动信息 Object
        String itemKillKey = RedisKeyUtil.getItemKillKey(itemKill.getId());
        redisTemplate.opsForValue().set(itemKillKey,gson.toJson(itemKill,ItemKill.class),itemKill.getEndTime().getTime()-now.getTime(), TimeUnit.MILLISECONDS);


        //redis 添加秒杀商品信息 Object
        String itemKey = RedisKeyUtil.getItemKey(item.getId());
        redisTemplate.opsForValue().set(itemKey,gson.toJson(item,Item.class),itemKill.getEndTime().getTime()-now.getTime(), TimeUnit.MILLISECONDS);


        return itemKill;
    }

    @Override
    public List<ItemKill> queryByStartTime(String startTime, Integer itemId) {
        return itemKillMapper.queryByStartTime(startTime,itemId);
    }
}
