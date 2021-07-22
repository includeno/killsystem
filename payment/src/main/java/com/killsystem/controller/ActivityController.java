package com.killsystem.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.killsystem.entity.Item;
import com.killsystem.entity.ItemKill;
import com.killsystem.entity.User;
import com.killsystem.flow.ItemFlow;
import com.killsystem.flow.ItemKillFlow;
import com.killsystem.flow.UserFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ActivityController {
    @Autowired
    ItemFlow itemFlow;

    @Autowired
    ItemKillFlow itemKillFlow;

    @Autowired
    UserFlow userFlow;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/activity/prepare")
    public HashMap<String,Integer> prepare(Integer itemCount,Integer period,Boolean clearRedis){
        Date now=new Date();
        //清除redis内所有key
        if(clearRedis.equals(Boolean.TRUE)){
            clearRedis();
        }

        //1添加商品
        Item item=generateItem();
        itemFlow.add(item);
        QueryWrapper<Item> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("code", item.getCode());

        List<Item> itemIds=itemFlow.queryByCondition(queryWrapper);
        Integer itemId=itemIds.get(0).getId();

        //2添加秒杀活动和redis预热
        ItemKill itemKill=generateItemKill();
        itemKill.setItemId(itemId);
        itemKill.setTotal(itemCount);
        itemKill.setStartTime(now);
        Date newDate = DateUtil.offset(now, DateField.MINUTE, period);
        itemKill.setEndTime(newDate);//starttime+30分钟
        itemKillFlow.create(itemKill);

        List<ItemKill> itemKillIds=itemKillFlow.queryByStartTime(DateUtil.format(itemKill.getStartTime(),"yyyy-MM-dd HH:mm:ss"),itemId);
        //System.out.println("itemKillIds: "+itemKillIds.size());
        Integer itemKillId=itemKillIds.get(0).getId();

        HashMap<String,Integer> map=new HashMap<>();
        map.put("itemId",itemId);
        map.put("itemKillId",itemKillId);
        return map;
    }

    @GetMapping("/activity/generateUsers")
    public String generateUsers(Integer count){
        //Integer count=1000;

        for(int i=0;i<count;i++){
            User user=getDefaultUser();
            userFlow.add(user);
        }
        return "success";
    }




    public Item generateItem(){
        Item item=new Item();
        Date now=new Date();
        item.setCode(UUID.randomUUID().toString());
        item.setIsActive(Item.active);
        item.setName(UUID.randomUUID().toString());
        item.setPurchaseTime(now);
        item.setStock(100L);
        item.setVersion(0);

        item.setCreateTime(now);
        item.setUpdateTime(now);
        return item;
    }

    public ItemKill generateItemKill(){
        ItemKill itemKill=new ItemKill();
        Date now=new Date();
        itemKill.setIsActive(ItemKill.active);
        itemKill.setItemId(1);
        itemKill.setTotal(10);
        itemKill.setVersion(0);

        itemKill.setStartTime(now);
        itemKill.setEndTime(now);
        itemKill.setCreateTime(now);
        itemKill.setUpdateTime(now);

        return itemKill;
    }

    public void clearRedis(){
        String allKey = "sale*";//这个*一定要加，否则无法模糊查询
        //模糊查询开头为map的所有key值
        Set<String> keys = redisTemplate.keys(allKey);
        //循环查到的所有的key
        if(keys!=null&&keys.size()>0){
            for(String key : keys){
                System.out.println(key);
                //删除
                redisTemplate.delete(key);
            }
        }else{
            System.out.println("没有该类型缓存");
        }

    }



    public void createActivity(){
        Date now=new Date();
        //清除redis内所有key
        clearRedis();

        //1添加商品
        Item item=generateItem();
        itemFlow.add(item);
        QueryWrapper<Item> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("code", item.getCode());

        List<Item> itemIds=itemFlow.queryByCondition(queryWrapper);
        Integer itemId=itemIds.get(0).getId();

        //2添加秒杀活动和redis预热
        ItemKill itemKill=generateItemKill();
        itemKill.setItemId(itemId);
        itemKill.setTotal(5);
        itemKill.setStartTime(now);
        Date newDate = DateUtil.offset(now, DateField.MINUTE, 20);
        itemKill.setEndTime(newDate);//starttime+30分钟
        itemKillFlow.create(itemKill);

    }

    public User getDefaultUser(){
        Date now=new Date();
        User user=new User();
        user.setUsername(String.valueOf(UUID.randomUUID()));
        user.setEmail("121212@126.com");
        user.setIsActive(User.active);
        user.setIsHidden(User.inactive);
        user.setMoney(User.defaultmoney);
        user.setPassword(String.valueOf(UUID.randomUUID()));
        user.setPhone(String.valueOf(UUID.randomUUID()));

        user.setCreateTime(now);
        user.setUpdateTime(now);

        return user;
    }
}
