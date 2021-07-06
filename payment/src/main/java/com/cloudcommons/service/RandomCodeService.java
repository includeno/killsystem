package com.cloudcommons.service;

import com.cloudcommons.entity.RandomCode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author includeno
 * @since 2021-02-16
 */
public interface RandomCodeService extends IService<RandomCode> {
    public boolean findSnowId(Long code);
}
