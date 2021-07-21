package com.killsystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.killsystem.entity.ItemKillSuccess;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 秒杀成功订单表 服务类
 * </p>
 *
 * @author includeno
 * @since 2021-02-16
 */
public interface ItemKillSuccessService extends IService<ItemKillSuccess> {


    public IPage<ItemKillSuccess> findList(IPage<ItemKillSuccess> page);


}
