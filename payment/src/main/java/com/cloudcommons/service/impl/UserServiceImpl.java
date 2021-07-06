package com.cloudcommons.service.impl;

import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloudcommons.entity.User;

import com.cloudcommons.exception.CustomException;
import com.cloudcommons.mapper.UserMapper;

import com.cloudcommons.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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
