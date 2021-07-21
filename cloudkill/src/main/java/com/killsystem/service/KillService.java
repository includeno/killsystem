package com.killsystem.service;

import com.killsystem.entity.ItemKillSuccess;

public interface KillService {
    ItemKillSuccess kill(Integer killId, Integer userId);
}
