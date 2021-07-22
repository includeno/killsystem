package com.killsystem.service;

import com.killsystem.entity.ItemKill;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 待秒杀商品表 服务类
 * </p>
 */
@Transactional
public interface ItemKillService extends IService<ItemKill> {
    //发布订单活动(管理员操作)
    ItemKill createKill(ItemKill itemKill);

    //根据开始时间和商品id进行查询
    List<ItemKill> queryByStartTime(String startTime,Integer itemId);
}
