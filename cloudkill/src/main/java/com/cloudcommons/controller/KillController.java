package com.cloudcommons.controller;


import com.cloudcommons.exception.R;
import com.cloudcommons.service.ItemKillSuccessService;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class KillController {

    //订单service
    @Autowired
    private ItemKillSuccessService itemKillSuccessService;

    //创建令牌桶
    private RateLimiter rateLimiter=RateLimiter.create(10);//每秒放行十个请求

    //无需登陆的秒杀业务
    @GetMapping("/sale")
    public R killNoLoggedIn(@RequestParam("killId") Integer killId, @RequestParam("userId") Integer userId){
        log.info("killId:"+killId);
        log.info("userId:"+userId);
        if (rateLimiter.tryAcquire(2, TimeUnit.SECONDS)) {
            //若获取乐观锁，service层实现
            Map<String, Object> map = itemKillSuccessService.kill(killId, userId);

            return R.ok("抢购成功！").data(map);
        }else {
            //System.out.println("当前请求已被限流，直接抛弃，无法调用后续业务"+count++);
            return R.error("令牌桶未获取到 当前商品抢购过于火爆~ 请重试！");
        }
    }
    //ItemKillSuccess killId秒杀id
    @GetMapping("/md5/{killId}/{userId}")
    public R getMd5(@PathVariable("killId")Integer killId, @PathVariable("userId")Integer userId){
        String md5;
        md5 = itemKillSuccessService.getMd5(killId,userId);
        return R.ok("获取md5成功").data("md5",md5);
    }
}
