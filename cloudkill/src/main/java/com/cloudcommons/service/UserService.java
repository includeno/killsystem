package com.cloudcommons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcommons.entity.User;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 */

public interface UserService extends IService<User> {
    User findOne(String username);


}
