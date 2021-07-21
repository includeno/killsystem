package com.killsystem.controller;

import com.killsystem.entity.User;
import com.killsystem.exception.R;
import com.killsystem.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "测试接口", tags = "用户管理相关的接口", description = "用户测试接口")
@RestController
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/user/{id}")
    @ApiOperation(value = "获取用户", notes = "获取用户")
    public R getUser(@PathVariable("id") int id){
        User entity=userService.getById(id);
        return R.ok().data("user",entity);
    }


    @PostMapping("/user")
    @ApiOperation(value = "添加用户", notes = "添加用户")
    public R addUser(User user){
        userService.save(user);
        return R.ok();
    }

    @PutMapping("/user")
    @ApiOperation(value = "更新用户", notes = "更新用户")
    public R updateUser(User user){
        userService.updateById(user);
        return R.ok();
    }

    @DeleteMapping("/user/{id}")
    @ApiOperation(value = "删除用户", notes = "删除用户")
    public R deleteUser(@PathVariable("id") int id){
        userService.removeById(id);
        return R.ok();
    }




}
