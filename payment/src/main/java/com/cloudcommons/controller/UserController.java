package com.cloudcommons.controller;

import com.cloudcommons.entity.User;
import com.cloudcommons.exception.R;
import com.cloudcommons.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    public R getUser(@PathVariable("id") int id){
        User entity=userService.getById(id);
        return R.ok().data("user",entity);
    }

    @PostMapping("/user")
    public R addUser(User user){
        userService.save(user);
        return R.ok();
    }

    @PutMapping("/user")
    public R updateUser(User user){
        userService.updateById(user);
        return R.ok();
    }

    @DeleteMapping("/user/{id}")
    public R deleteUser(@PathVariable("id") int id){
        userService.removeById(id);
        return R.ok();
    }




}
