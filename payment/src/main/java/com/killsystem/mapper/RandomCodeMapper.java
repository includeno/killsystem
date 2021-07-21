package com.killsystem.mapper;

import com.killsystem.entity.RandomCode;
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
