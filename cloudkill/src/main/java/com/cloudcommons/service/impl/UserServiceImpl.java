package com.cloudcommons.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcommons.entity.User;
import com.cloudcommons.mapper.UserMapper;
import com.cloudcommons.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User findOne(String username) {
        return baseMapper.selectOne(new QueryWrapper<User>().eq("user_name",username));
    }


}
