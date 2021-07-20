package com.cloudcommons.flow;

import com.cloudcommons.PaymentMain;
import com.cloudcommons.entity.ItemKill;
import com.cloudcommons.exception.ItemKillException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = PaymentMain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemKillFlowTest {
    @Autowired
    ItemKillFlow itemKillFlow;


    public ItemKill getDefault(){
        ItemKill itemKill=new ItemKill();

        itemKill.setIsActive(ItemKill.active);
        itemKill.setItemId(1);
        itemKill.setTotal(10);
        itemKill.setVersion(0);

        itemKill.setStartTime(new Date());
        itemKill.setEndTime(new Date());
        itemKill.setCreateTime(new Date());
        itemKill.setUpdateTime(new Date());

        return itemKill;
    }

    public ItemKill getError(){
        ItemKill itemKill=new ItemKill();

        itemKill.setIsActive(ItemKill.active);
        itemKill.setItemId(1);
        itemKill.setTotal(10);
        itemKill.setVersion(0);

        itemKill.setStartTime(new Date());
        itemKill.setEndTime(new Date());
        itemKill.setCreateTime(new Date());
        itemKill.setUpdateTime(new Date());

        return itemKill;
    }

    @Test
    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    public void add() {
        ItemKill itemKill1=getDefault();
        Boolean result=itemKillFlow.add(itemKill1);
        Assert.assertEquals(true,result);

        ItemKill itemKill2=getDefault();
        itemKill2.setItemId(3);
        Boolean result2=null;
        try {
            result2=itemKillFlow.add(itemKill1);
        }
        catch (ItemKillException e){
            result2=Boolean.FALSE;
        }
        finally {
            Assert.assertEquals(Boolean.TRUE,result2);
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    @Test
    public void update() {

        ItemKill itemKill1=getDefault();
        Boolean result=itemKillFlow.add(itemKill1);
        Assert.assertEquals(true,result);

        List<ItemKill> list = itemKillFlow.list();
        Assert.assertEquals(1,list.size());

        ItemKill itemKill2=list.get(0);
        itemKill2.setCreateTime(new Date());
        System.out.println(itemKill2);
        itemKillFlow.update(itemKill2);

        list = itemKillFlow.list();
        Assert.assertEquals(1,list.size());
        ItemKill temp=list.get(0);
        System.out.println(temp);
        Assert.assertEquals(itemKill2.getIsActive(),(temp.getIsActive()));
    }

    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    @Test
    public void queryById() {

        ItemKill itemKill1=getDefault();
        Boolean result=itemKillFlow.add(itemKill1);
        Assert.assertEquals(true,result);

        List<ItemKill> list = itemKillFlow.list();
        Assert.assertEquals(1,list.size());
        ItemKill itemKill2=list.get(0);

        ItemKill temp=itemKillFlow.queryById(itemKill2.getId());
        Assert.assertEquals(itemKill2,temp);
    }

    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    @Test
    public void list() {

        ItemKill itemKill1=getDefault();
        Boolean result=itemKillFlow.add(itemKill1);
        Assert.assertEquals(true,result);

        List<ItemKill> list = itemKillFlow.list();
        Assert.assertEquals(1,list.size());
        ItemKill itemKill2=list.get(0);

        itemKill1.setId(itemKill2.getId());

        itemKill1.setCreateTime(itemKill2.getCreateTime());
        itemKill1.setUpdateTime(itemKill2.getUpdateTime());
        itemKill1.setStartTime(itemKill2.getStartTime());
        itemKill1.setEndTime(itemKill2.getEndTime());
        Assert.assertEquals(itemKill1,itemKill2);
    }

    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    @Test
    public void queryByCondition() {
    }

    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    @Test
    public void pageQuery() {
    }

    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    @Test
    public void deleteById() {

        ItemKill itemKill1=getDefault();
        Boolean result=itemKillFlow.add(itemKill1);
        Assert.assertEquals(true,result);

        List<ItemKill> list = itemKillFlow.list();
        Assert.assertEquals(1,list.size());

        ItemKill itemKill2=list.get(0);
        itemKillFlow.deleteById(itemKill2.getId());

        list = itemKillFlow.list();
        Assert.assertEquals(0,list.size());

    }
}