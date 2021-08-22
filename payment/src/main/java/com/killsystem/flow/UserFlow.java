package com.killsystem.flow;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.killsystem.entity.User;
import com.killsystem.exception.Status;
import com.killsystem.exception.UserException;
import com.killsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFlow {
    public Logger log= LoggerFactory.getLogger("UserFlow");

    @Autowired
    UserService userService;

    public Boolean add(User user){
        if (user!=null && !user.getUsername().isEmpty() ){
            User temp = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
            if (temp!=null){
                throw new UserException(Status.USER_NAME_EXIST);//存在重名
            }
            return userService.save(user);
        }
        //不符合添加标准
        throw new UserException(Status.ITEM_NOT_ALLOWED);//不符合条件
    }

    public Boolean update(User item){
        User  queryResult= userService.getById(item.getId());
        if(queryResult!=null){
            return userService.updateById(item);
        }
        else{
            log.warn("no changes in user");
            return true;
        }

    }

    public User queryById(Integer id){
        return userService.getById(id);
    }

    public List<User> list(){
        return userService.list();
    }
    public List<User> queryByCondition(Wrapper<User> queryWrapper){
        return userService.list(queryWrapper);
    }

    public IPage<User> pageQuery(IPage<User> queryPage, Wrapper<User> itemQueryWrapper){
        IPage<User> itemKillIPage = userService.page(queryPage, itemQueryWrapper);
        return itemKillIPage;
    }

    public Boolean deleteById(Integer id){
        return userService.removeById(id);
    }

}
