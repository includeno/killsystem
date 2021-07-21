package com.killsystem.controller;

//订单接口

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.killsystem.entity.ItemKillSuccess;
import com.killsystem.exception.R;
import com.killsystem.service.ItemKillSuccessService;
import com.killsystem.vo.ItemKillSuccessQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@Api(value = "秒杀成功订单接口", tags = "秒杀成功订单相关的接口", description = "秒杀成功订单测试接口")
public class ItemKillSuccessController {

    @Autowired
    private ItemKillSuccessService iksService;

    @PostMapping("/getItemKillSuccessList")
    @ApiOperation(value = "分页查询秒杀成功订单", notes = "分页查询秒杀成功订单")
    public R getIksList(@RequestBody ItemKillSuccessQuery iksQuery){
        int pageNo=(iksQuery.getPage()-1)*iksQuery.getLimit();

        int pageSize=5;
        IPage<ItemKillSuccess> page = new Page<>(pageNo, pageSize);
        IPage<ItemKillSuccess> result=iksService.findList(page);
        System.out.println(" IPage<ItemKillSuccess> result:"+result);
        HashMap<String,Object> map=new HashMap<>();
        map.put("list",result);
        return R.ok().data(map);
    }
}
