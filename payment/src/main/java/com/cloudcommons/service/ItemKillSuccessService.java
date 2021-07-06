package com.cloudcommons.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudcommons.entity.ItemKillSuccess;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 秒杀成功订单表 服务类
 * </p>
 *
 * @author includeno
 * @since 2021-02-16
 */
public interface ItemKillSuccessService extends IService<ItemKillSuccess> {

    Map<String, Object> kill(Integer killId, Integer userId);

    Map<String, Object> kill2(Integer killId, Integer userId, String md5);

    String getMd5(Integer killId, Integer userId);

    public IPage<ItemKillSuccess> findList(IPage<ItemKillSuccess> page);


}
