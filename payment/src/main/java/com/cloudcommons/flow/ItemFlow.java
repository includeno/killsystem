package com.cloudcommons.flow;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudcommons.entity.Item;
import com.cloudcommons.exception.ItemException;
import com.cloudcommons.exception.Status;
import com.cloudcommons.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ItemFlow {

    @Autowired
    ItemService itemService;

    public Boolean add(Item item){
        if (item!=null && !item.getName().isEmpty() && item.getStock()>=0 && !item.getCode().isEmpty()){
            Item temp = itemService.getOne(new QueryWrapper<Item>().eq("name", item.getName()));
            if (temp!=null){
                throw new ItemException(Status.ITEM_NAME_EXIST);//存在重名
            }
            return itemService.save(item);
        }
        //不符合添加标准
        throw new ItemException(Status.ITEM_NOT_ALLOWED);//不符合条件
    }

    public Boolean update(Item item){
        Item  queryResult=itemService.getById(item.getId());
        if(queryResult!=null){
            return itemService.updateById(item);
        }
        else{
            log.warn("no changes in item");
            return true;
        }

    }

    public Item queryById(Integer id){
        return itemService.getById(id);
    }

    public List<Item> list(){
        return itemService.list();
    }
    public List<Item> queryByCondition(Wrapper<Item> queryWrapper){
        return itemService.list(queryWrapper);
    }

    public IPage<Item> pageQuery(IPage<Item> queryPage,Wrapper<Item> itemQueryWrapper){
        IPage<Item> itemKillIPage = itemService.page(queryPage, itemQueryWrapper);
        return itemKillIPage;
    }

    public Boolean deleteById(Integer id){
        return itemService.removeById(id);
    }

}
