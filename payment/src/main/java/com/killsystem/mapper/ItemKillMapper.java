package com.killsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.killsystem.entity.ItemKill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 待秒杀商品表 Mapper 接口
 * </p>
 *
 * @author includeno
 * @since 2021-02-16
 */
@Repository
public interface ItemKillMapper extends BaseMapper<ItemKill> {

    List<ItemKill> queryByStartTime(@Param("startTime")String startTime, @Param("itemId")Integer itemId);
}
