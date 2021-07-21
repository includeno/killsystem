package com.killsystem.controller;

import com.killsystem.entity.Item;
import com.killsystem.exception.R;
import com.killsystem.flow.ItemFlow;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    ItemFlow itemFlow;

    //添加商品
    @PostMapping("/item")
    @ApiOperation(value = "添加商品", notes = "添加商品")
    public R addItem(Item item){
        boolean save = itemFlow.add(item);
        return save?R.ok():R.error();
    }

    //商品
    @PutMapping("/item")
    public R updateItem(Item item){
        boolean save = itemFlow.update(item);
        return save?R.ok():R.error();
    }

    //获取单个商品
    @GetMapping("/item/{id}")
    public R getItem(@PathVariable("id") int id){
        Item item = itemFlow.queryById(id);
        return R.ok().data("item",item);
    }

    //获取所有商品列表
    @GetMapping("/items")
    public R list(){
        return R.ok().data("items",itemFlow.list());
    }

    //删除商品
    @PostMapping("/item/{id}")
    public R removeItem(@PathVariable("id") int id){
        boolean res = itemFlow.deleteById(id);
        return res?R.ok().message("删除成功!"):R.error().message("删除失败");
    }
}
