package com.cloudcommons.controller;

//订单接口

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcommons.entity.ItemKillSuccess;
import com.cloudcommons.exception.R;
import com.cloudcommons.service.ItemKillSuccessService;
import com.cloudcommons.vo.ItemKillSuccessQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class ItemKillSuccessController {

    @Autowired
    private ItemKillSuccessService iksService;

    @PostMapping("/getItemKillSuccessList")
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
