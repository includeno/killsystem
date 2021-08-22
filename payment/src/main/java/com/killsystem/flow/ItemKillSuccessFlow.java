package com.killsystem.flow;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.killsystem.entity.ItemKillSuccess;
import com.killsystem.service.ItemKillSuccessService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemKillSuccessFlow {
    public Logger log= LoggerFactory.getLogger("ItemKillSuccessFlow");

    @Autowired
    ItemKillSuccessService itemKillSuccessService;

    public Boolean add(ItemKillSuccess itemKillSuccess){

        if (itemKillSuccess!=null){
            ItemKillSuccess temp = itemKillSuccessService.getById(itemKillSuccess.getId());
            return itemKillSuccessService.save(itemKillSuccess);
        }
        else {
            return Boolean.FALSE;
        }
    }

    public Boolean update(ItemKillSuccess itemKillSuccess){
        ItemKillSuccess  queryResult=itemKillSuccessService.getById(itemKillSuccess.getId());
        if(queryResult!=null){
            return itemKillSuccessService.updateById(itemKillSuccess);
        }
        else{
            log.warn("no changes in ItemKillSuccess");
            return true;
        }

    }

    public ItemKillSuccess queryById(Integer id){
        return itemKillSuccessService.getById(id);
    }

    public List<ItemKillSuccess> list(){
        return itemKillSuccessService.list();
    }
    public List<ItemKillSuccess> queryByCondition(Wrapper<ItemKillSuccess> queryWrapper){
        return itemKillSuccessService.list(queryWrapper);
    }

    public IPage<ItemKillSuccess> pageQuery(IPage<ItemKillSuccess> queryPage, Wrapper<ItemKillSuccess> ItemKillSuccessQueryWrapper){
        IPage<ItemKillSuccess> ItemKillSuccessKillIPage = itemKillSuccessService.page(queryPage, ItemKillSuccessQueryWrapper);
        return ItemKillSuccessKillIPage;
    }

    public Boolean deleteById(Integer id){
        return itemKillSuccessService.removeById(id);
    }

}
