package com.killsystem.service;

import com.killsystem.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author includeno
 * @since 2021-02-16
 */
public interface UserService extends IService<User> {
    User findOne(String username);


}
