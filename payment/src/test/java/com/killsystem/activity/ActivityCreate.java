package com.killsystem.activity;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.killsystem.PaymentMain;
import com.killsystem.entity.Item;
import com.killsystem.entity.ItemKill;
import com.killsystem.entity.User;
import com.killsystem.flow.ItemFlow;
import com.killsystem.flow.ItemKillFlow;
import com.killsystem.flow.UserFlow;
import com.killsystem.mapper.ItemKillMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@SpringBootTest(classes = PaymentMain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActivityCreate {
    @Autowired
    ItemFlow itemFlow;

    @Autowired
    ItemKillFlow itemKillFlow;

    @Autowired
    UserFlow userFlow;

    @Autowired
    ItemKillMapper itemKillMapper;

    @Autowired
    private RedisTemplate redisTemplate;

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

    @Test
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

        List<ItemKill> itemKillIds=itemKillMapper.queryByStartTime(DateUtil.format(itemKill.getStartTime(),"yyyy-MM-dd HH:mm:ss"),itemId);
        System.out.println("itemKillIds: "+itemKillIds.size());

//        QueryWrapper<ItemKill> itemKillQueryWrapper=new QueryWrapper<>();
//        String strStart= DateFormatUtils.format(itemKill.getStartTime(),"yyyy-MM-dd HH:mm:ss");
//        //itemKillQueryWrapper.eq("start_time", itemKill.getStartTime());
//        itemKillQueryWrapper.apply(("UNIX_TIMESTAMP(start_time)=UNIX_TIMESTAMP('"+strStart+"')"));
//        itemKillIds=itemKillFlow.queryByCondition(itemKillQueryWrapper);
        Integer itemKillId=itemKillIds.get(0).getId();

        System.out.println("itemId:"+itemId+" itemKillId"+itemKillId);

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
    @Test
    public void createUser(){
        User user=getDefaultUser();
        userFlow.add(user);
    }

    /*
    *   批量生成用户
    * */
    @Test
    public void createBatchUser(){
        Integer count=1000;

        for(int i=0;i<count;i++){
            User user=getDefaultUser();
            userFlow.add(user);
        }
    }
}
