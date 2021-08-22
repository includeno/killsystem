package com.killsystem.service;

import com.killsystem.entity.ItemKillSuccess;

import java.util.concurrent.TimeUnit;

public interface KillService {
    ItemKillSuccess kill(Integer killId, Integer userId);

    ItemKillSuccess killByMysql(Integer killId, Integer userId);

    Boolean examUser(Integer userId);

    Boolean examUserInPeriod(Integer userId, Integer period, TimeUnit unit, Integer max);
}
