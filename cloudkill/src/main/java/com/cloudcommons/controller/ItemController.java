package com.cloudcommons.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloudcommons.entity.Item;
import com.cloudcommons.exception.R;
import com.cloudcommons.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author includeno
 * @since 2021/3/23 13:03
 */
@RestController
@Slf4j
public class ItemController {

    @Autowired
    ItemService itemService;

    //获取所有商品列表
    @GetMapping("getItems")
    public R getItems(){
        return R.ok().data("items",itemService.list(null));
    }



    //获取单个商品
    @GetMapping("/getItem/{id}")
    public R getItem(@PathVariable("id") int id){
        Item item = itemService.getById(id);
        return R.ok().data("item",item);
    }

    //添加商品
    @PostMapping("/addItem")
    public R addItem(Item item){
        if (item!=null && !item.getName().isEmpty() && item.getStock()>=0 && !item.getCode().isEmpty()){
            Item item1 = itemService.getOne(new QueryWrapper<Item>().eq("name", item.getName()));
            if (item1!=null){
                return R.error("已存在该商品");
            }
            boolean save = itemService.save(item);
            return save?R.ok():R.error();
        }
        return R.error();
    }

    //删除商品
    @DeleteMapping("/removeItem/{id}")
    public R removeItem(@PathVariable("id") int id){
        //查询是否存在
        boolean res = itemService.removeById(id);
        return res?R.ok().message("删除成功!"):R.error().message("删除失败");
    }
}
