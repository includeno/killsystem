package com.cloudcommons.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcommons.entity.Item;
import com.cloudcommons.entity.ItemKill;
import com.cloudcommons.exception.R;
import com.cloudcommons.service.ItemKillService;
import com.cloudcommons.service.ItemService;
import com.cloudcommons.vo.ItemKillVO;
import com.cloudcommons.vo.ItemQuery;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ItemKillController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemKillService itemKillService;

    //查商品列表
    @PostMapping("/getKillList/{page}/{limit}")
    @ApiOperation(value = "分页查询秒杀商品", notes = "分页查询秒杀商品")
    public R getKillList(@PathVariable("page") int page, @PathVariable("limit") int limit, @RequestBody ItemQuery itemQuery){
        Page<ItemKill> queryPage=new Page<>(page,limit);//分页器
        //先查询商品
        List<Item> items = itemService.list(new QueryWrapper<Item>().like("name", itemQuery.getName()).or()
                .like("code",itemQuery.getCode()));
        //通过商品查询活动
        QueryWrapper<ItemKill> itemKillQueryWrapper=null;
        if (items!=null && items.size()>0){
            itemKillQueryWrapper=new QueryWrapper<ItemKill>();
            List<Integer> list=new ArrayList<>();
            for(Item item: items){
                list.add(item.getId());
            }
            itemKillQueryWrapper.in("item_id",list);
        }

        //对活动进行分页
        IPage<ItemKill> itemKillIPage = itemKillService.page(queryPage, itemKillQueryWrapper);
        List<ItemKill> records = itemKillIPage.getRecords();
        //将结果集重写封装
        if (records!=null && records.size()>0){
            List<ItemKillVO> itemKills=new ArrayList<>();
            for(ItemKill itemKill:records){

                Item item = itemService.getById(itemKill.getItemId());
                ItemKillVO itemKillVo=new ItemKillVO();
                BeanUtil.copyProperties(itemKill,itemKillVo);
                itemKillVo.setName(item.getName());
                itemKills.add(itemKillVo);
            }
            return R.ok().data("itemKills",itemKills)
                    .data("total",itemKillIPage.getTotal());
        }
        return R.ok("无数据");
    }

    //删除活动
    @PostMapping("/removeItemKIll/{id}")
    public R removeItemKIll(@PathVariable("id") int id){
        boolean res = itemKillService.removeById(id);
        return res?R.ok().message("删除成功!"):R.error().message("删除失败");
    }

    //获取单个活动
    @GetMapping("/getItemKill/{id}")
    public R getItemKill(@PathVariable("id") int id){
        ItemKill itemKill = itemKillService.getById(id);
        return R.ok().data("itemKill",itemKill);
    }

    //获取单个活动与商品
    @GetMapping("/getItemKillAndItem/{id}")
    public R getItemKillAndItem(@PathVariable("id") int id){
        ItemKill itemKill = itemKillService.getById(id);
        Item item = itemService.getById(itemKill.getItemId());
        ItemKillVO itemKillVo=new ItemKillVO();
        BeanUtil.copyProperties(itemKill,itemKillVo);
        itemKillVo.setName(item.getName());
        return R.ok().data("itemKill",itemKillVo);
    }

    //添加秒杀活动商品，同一个商品可以添加多次
    @PostMapping("/addItemKill")
    public R addItemKill(ItemKill itemKill){
        if (itemKill!=null && itemKill.getItemId()!=null && itemKill.getTotal()>=0){
            //boolean save = itemKillService.save(itemKill);
            ItemKill kill = itemKillService.createKill(itemKill);
            return R.ok().data("itemKill",itemKill);
        }
        return R.error();
    }

    //修改秒杀活动商品，只能对单个商品信息进行修改
    @PostMapping("/updateItemKill")
    public R updateItemKill(ItemKill itemKill){
        if (itemKill!=null && itemKill.getItemId()!=null && itemKill.getTotal()>=0){
            boolean save = itemKillService.update(itemKill,new QueryWrapper<ItemKill>().eq("id",itemKill.getId()));
            //TODO 更新缓存
            return save?R.ok():R.error();
        }
        return R.error();
    }

}
