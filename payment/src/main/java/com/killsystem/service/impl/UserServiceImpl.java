package com.killsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.killsystem.entity.User;

import com.killsystem.mapper.UserMapper;

import com.killsystem.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author includeno
 * @since 2021-02-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User findOne(String username) {
        return baseMapper.selectOne(new QueryWrapper<User>().eq("user_name",username));
    }


}
