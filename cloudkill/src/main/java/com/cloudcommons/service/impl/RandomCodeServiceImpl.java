package com.cloudcommons.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcommons.entity.RandomCode;
import com.cloudcommons.mapper.RandomCodeMapper;
import com.cloudcommons.service.RandomCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 */
@Service
public class RandomCodeServiceImpl extends ServiceImpl<RandomCodeMapper, RandomCode> implements RandomCodeService {
    @Autowired
    private RandomCodeMapper mapper;

    @Override
    public boolean findSnowId(Long code) {
        RandomCode randomCode = mapper.findSnowId(Convert.toStr(code));
        if(randomCode==null){
            return false;
        }else {
            return true;
        }
    }
}
