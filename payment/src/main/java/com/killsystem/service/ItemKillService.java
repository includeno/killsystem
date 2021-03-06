package com.killsystem.service;

import com.killsystem.entity.ItemKill;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 待秒杀商品表 服务类
 * </p>
 */
@Transactional
public interface ItemKillService extends IService<ItemKill> {
    //发布订单活动(管理员操作)
    ItemKill createKill(ItemKill itemKill);
}
