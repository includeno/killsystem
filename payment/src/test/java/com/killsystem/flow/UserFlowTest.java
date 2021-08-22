package com.killsystem.flow;

import com.killsystem.PaymentMain;
import com.killsystem.entity.User;
import com.killsystem.exception.UserException;
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
class UserFlowTest {
    public Logger log= LoggerFactory.getLogger("UserFlowTest");

    @Autowired
    UserFlow userFlow;

    public User getDefaultUser(){
        User user=new User();
        user.setUsername(String.valueOf(UUID.randomUUID()));
        user.setEmail("121212@126.com");
        user.setIsActive(User.active);
        user.setIsHidden(User.inactive);
        user.setMoney(User.defaultmoney);
        user.setPassword(String.valueOf(UUID.randomUUID()));
        user.setPhone(String.valueOf(UUID.randomUUID()));

        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        return user;
    }

    public User getErrorUser(){
        User user=new User();
        user.setUsername(String.valueOf(UUID.randomUUID()));
        user.setEmail("121212@126.com");
        user.setIsActive(User.active);
        user.setIsHidden(User.inactive);
        user.setMoney(User.defaultmoney);
        user.setPassword(String.valueOf(UUID.randomUUID()));
        user.setPhone(String.valueOf(UUID.randomUUID()));

        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        return user;
    }

    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    @Test
    void add() {
        User user1=getDefaultUser();
        Boolean result=userFlow.add(user1);
        Assert.assertEquals(true,result);

        User user2=getDefaultUser();
        user2.setUsername(user1.getUsername());
        Boolean result2=null;
        try {
            result2=userFlow.add(user1);
        }
        catch (UserException e){
            result2=Boolean.FALSE;
        }
        finally {
            Assert.assertEquals(Boolean.FALSE,result2);
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    @Test
    void update() {

        User user1=getDefaultUser();
        Boolean result=userFlow.add(user1);
        Assert.assertEquals(true,result);

        List<User> list = userFlow.list();
        Assert.assertEquals(1,list.size());

        User user2=list.get(0);
        user2.setEmail(UUID.randomUUID().toString());
        userFlow.update(user2);

        list = userFlow.list();
        Assert.assertEquals(1,list.size());
        User temp=list.get(0);
        Assert.assertEquals(user2,temp);
    }

    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    @Test
    void queryById() {

        User user1=getDefaultUser();
        Boolean result=userFlow.add(user1);
        Assert.assertEquals(true,result);

        List<User> list = userFlow.list();
        Assert.assertEquals(1,list.size());
        User user2=list.get(0);

        User temp=userFlow.queryById(user2.getId());
        Assert.assertEquals(user2,temp);
    }

    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    @Test
    void list() {

        User user1=getDefaultUser();
        Boolean result=userFlow.add(user1);
        Assert.assertEquals(true,result);

        List<User> list = userFlow.list();
        Assert.assertEquals(1,list.size());
        User user2=list.get(0);

        user1.setId(user2.getId());
        user1.setUpdateTime(user2.getUpdateTime());
        user1.setCreateTime(user2.getCreateTime());
        Assert.assertEquals(user1,user2);
    }

    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    @Test
    void queryByCondition() {
    }

    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    @Test
    void pageQuery() {
    }

    @Transactional(propagation = Propagation.NESTED)
    @Rollback(value = true)
    @Test
    void deleteById() {

        User user1=getDefaultUser();
        Boolean result=userFlow.add(user1);
        Assert.assertEquals(true,result);

        List<User> list = userFlow.list();
        Assert.assertEquals(1,list.size());

        User user2=list.get(0);
        userFlow.deleteById(user2.getId());

        list = userFlow.list();
        Assert.assertEquals(0,list.size());

    }
}