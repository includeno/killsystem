package com.cloudcommons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudcommons.entity.RandomCode;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 */

public interface RandomCodeService extends IService<RandomCode> {
    public boolean findSnowId(Long code);
}
