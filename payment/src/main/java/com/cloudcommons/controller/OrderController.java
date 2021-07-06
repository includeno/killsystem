package com.cloudcommons.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloudcommons.entity.ItemKillSuccess;
import com.cloudcommons.exception.R;
import com.cloudcommons.service.ItemKillSuccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public class OrderController {

    @Autowired
    private ItemKillSuccessService iksService;

    @GetMapping("/getOrders/{userId}")
    public R getOrders(@PathVariable("userId")Integer userId){
        List<ItemKillSuccess> orders = iksService.list(new QueryWrapper<ItemKillSuccess>().eq("user_id",userId));
        if (orders==null || orders.size()==0){
            return R.error("无订单信息");
        }else{
            return R.ok().data("orders",orders);
        }
    }
}
