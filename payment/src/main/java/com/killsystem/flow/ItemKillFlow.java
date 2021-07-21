package com.killsystem.flow;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.killsystem.entity.ItemKill;
import com.killsystem.exception.ItemKillException;
import com.killsystem.exception.Status;
import com.killsystem.service.ItemKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ItemKillFlow {


    @Autowired
    ItemKillService ItemKillService;

    public Boolean add(ItemKill itemKill){
        itemKill.setIsActive(ItemKill.active);
        if (itemKill!=null){
            ItemKill temp = ItemKillService.getById(itemKill.getId());
            return ItemKillService.save(itemKill);
        }
        //不符合添加标准
        throw new ItemKillException(Status.ITEMKILL_NOT_ALLOWED);//不符合条件
    }

    public ItemKill create(ItemKill itemKill){
        itemKill.setIsActive(ItemKill.active);
        if (itemKill!=null){
            //ItemKill temp = ItemKillService.getById(itemKill.getId());
            return ItemKillService.createKill(itemKill);
        }
        //不符合添加标准
        throw new ItemKillException(Status.ITEMKILL_NOT_ALLOWED);//不符合条件
    }

    public Boolean update(ItemKill itemKill){
        ItemKill  queryResult=ItemKillService.getById(itemKill.getId());
        if(queryResult!=null){
            return ItemKillService.updateById(itemKill);
        }
        else{
            log.warn("no changes in ItemKill");
            return true;
        }

    }

    public ItemKill queryById(Integer id){
        return ItemKillService.getById(id);
    }

    public List<ItemKill> list(){
        return ItemKillService.list();
    }
    public List<ItemKill> queryByCondition(Wrapper<ItemKill> queryWrapper){
        return ItemKillService.list(queryWrapper);
    }

    public IPage<ItemKill> pageQuery(IPage<ItemKill> queryPage, Wrapper<ItemKill> ItemKillQueryWrapper){
        IPage<ItemKill> ItemKillKillIPage = ItemKillService.page(queryPage, ItemKillQueryWrapper);
        return ItemKillKillIPage;
    }

    public Boolean deleteById(Integer id){
        return ItemKillService.removeById(id);
    }

}
