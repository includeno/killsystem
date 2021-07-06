package com.cloudcommons.service.impl;

import com.cloudcommons.entity.Item;
import com.cloudcommons.mapper.ItemMapper;
import com.cloudcommons.service.ItemService;
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
