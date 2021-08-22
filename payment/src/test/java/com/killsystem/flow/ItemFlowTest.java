package com.killsystem.flow;

import com.killsystem.PaymentMain;
import com.killsystem.entity.Item;
import com.killsystem.exception.ItemException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = PaymentMain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemFlowTest {
    public Logger log= LoggerFactory.getLogger("ItemFlowTest");

    @Autowired
    ItemFlow itemFlow;


    public Item getDefault(){
        Item item=new Item();
        item.setCode("dsdf");
        item.setIsActive(Item.active);
        item.setName(UUID.randomUUID().toString());
        item.setPurchaseTime(new Date());
        item.setStock(100L);
        item.setVersion(0);

        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());

        return item;
    }

    public Item getError(){
        Item item=new Item();
        item.setCode("dsdf");
        item.setIsActive(Item.active);
        item.setName(UUID.randomUUID().toString());
        item.setPurchaseTime(new Date());
        item.setStock(100L);
        item.setVersion(0);

        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());

        return item;
    }

    @Test
    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    public void add() {
        Item item1=getDefault();
        Boolean result=itemFlow.add(item1);
        Assert.assertEquals(true,result);

        Item item2=getDefault();
        item2.setName(item1.getName());
        Boolean result2=null;
        try {
            result2=itemFlow.add(item1);
        }
        catch (ItemException e){
            result2=Boolean.FALSE;
        }
        finally {
            Assert.assertEquals(Boolean.FALSE,result2);
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    @Test
    public void update() {

        Item item1=getDefault();
        Boolean result=itemFlow.add(item1);
        Assert.assertEquals(true,result);

        List<Item> list = itemFlow.list();
        Assert.assertEquals(1,list.size());

        Item item2=list.get(0);
        item2.setCode(UUID.randomUUID().toString());
        itemFlow.update(item2);

        list = itemFlow.list();
        Assert.assertEquals(1,list.size());
        Item temp=list.get(0);
        Assert.assertEquals(item2,temp);
    }

    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    @Test
    public void queryById() {

        Item item1=getDefault();
        Boolean result=itemFlow.add(item1);
        Assert.assertEquals(true,result);

        List<Item> list = itemFlow.list();
        Assert.assertEquals(1,list.size());
        Item item2=list.get(0);

        Item temp=itemFlow.queryById(item2.getId());
        Assert.assertEquals(item2,temp);
    }

    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    @Test
    public void list() {

        Item item1=getDefault();
        Boolean result=itemFlow.add(item1);
        Assert.assertEquals(true,result);

        List<Item> list = itemFlow.list();
        Assert.assertEquals(1,list.size());
        Item item2=list.get(0);

        item1.setId(item2.getId());
        item1.setUpdateTime(item2.getUpdateTime());
        item1.setCreateTime(item2.getCreateTime());
        Assert.assertEquals(item1,item2);
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

        Item item1=getDefault();
        Boolean result=itemFlow.add(item1);
        Assert.assertEquals(true,result);

        List<Item> list = itemFlow.list();
        Assert.assertEquals(1,list.size());

        Item item2=list.get(0);
        itemFlow.deleteById(item2.getId());

        list = itemFlow.list();
        Assert.assertEquals(0,list.size());

    }
}