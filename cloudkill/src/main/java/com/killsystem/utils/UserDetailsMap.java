package com.killsystem.utils;

import com.killsystem.entity.User;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author includeno
 * @since 2021/3/23 10:49
 */
public class UserDetailsMap {

    //存储登陆用户
    ConcurrentHashMap<String, User> usermap=new ConcurrentHashMap<>();


}
