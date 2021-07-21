package com.killsystem.service.impl;

import com.killsystem.entity.Item;
import com.killsystem.mapper.ItemMapper;
import com.killsystem.service.ItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 */
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

}
