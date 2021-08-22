package com.killsystem.flow;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.killsystem.entity.ItemKill;
import com.killsystem.exception.ItemKillException;
import com.killsystem.exception.Status;
import com.killsystem.service.ItemKillService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemKillFlow {
    public Logger log= LoggerFactory.getLogger("ItemKillFlow");

    @Autowired
    ItemKillService itemKillService;

    public Boolean add(ItemKill itemKill){
        itemKill.setIsActive(ItemKill.active);
        if (itemKill!=null){
            ItemKill temp = itemKillService.getById(itemKill.getId());
            return itemKillService.save(itemKill);
        }
        //不符合添加标准
        throw new ItemKillException(Status.ITEMKILL_NOT_ALLOWED);//不符合条件
    }

    public ItemKill create(ItemKill itemKill){
        itemKill.setIsActive(ItemKill.active);
        if (itemKill!=null){
            //ItemKill temp = ItemKillService.getById(itemKill.getId());
            return itemKillService.createKill(itemKill);
        }
        //不符合添加标准
        throw new ItemKillException(Status.ITEMKILL_NOT_ALLOWED);//不符合条件
    }

    public Boolean update(ItemKill itemKill){
        ItemKill  queryResult= itemKillService.getById(itemKill.getId());
        if(queryResult!=null){
            return itemKillService.updateById(itemKill);
        }
        else{
            log.warn("no changes in ItemKill");
            return true;
        }

    }

    public ItemKill queryById(Integer id){
        return itemKillService.getById(id);
    }

    public List<ItemKill> queryByStartTime(String startTime,Integer itemId){
        return itemKillService.queryByStartTime(startTime,itemId);
    }

    public List<ItemKill> list(){
        return itemKillService.list();
    }
    public List<ItemKill> queryByCondition(Wrapper<ItemKill> queryWrapper){
        return itemKillService.list(queryWrapper);
    }

    public IPage<ItemKill> pageQuery(IPage<ItemKill> queryPage, Wrapper<ItemKill> ItemKillQueryWrapper){
        IPage<ItemKill> ItemKillKillIPage = itemKillService.page(queryPage, ItemKillQueryWrapper);
        return ItemKillKillIPage;
    }

    public Boolean deleteById(Integer id){
        return itemKillService.removeById(id);
    }

}
