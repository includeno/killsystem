package com.cloudcommons.mapper;

import com.cloudcommons.entity.RandomCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author includeno
 * @since 2021-02-16
 */
public interface RandomCodeMapper extends BaseMapper<RandomCode> {

    RandomCode findSnowId(String toStr);
}
