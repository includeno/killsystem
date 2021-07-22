package com.killsystem.controller;


import com.killsystem.entity.ItemKillSuccess;
import com.killsystem.exception.R;
import com.killsystem.service.KillService;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class KillController {

    //订单service
    @Autowired
    private KillService killService;

    //创建令牌桶
    private RateLimiter rateLimiter=RateLimiter.create(20);//每秒放行十个请求

    //无需登陆的秒杀业务
    @GetMapping("/sale")
    public R killNoLoggedIn(@RequestParam("killId") Integer killId, @RequestParam("userId") Integer userId){
        log.info("killNoLoggedIn killId:"+killId+" userId:"+userId);
        //用户登录状态检测
        killService.examUserInPeriod(userId,1000,TimeUnit.MILLISECONDS,3);
        if (rateLimiter.tryAcquire(2, TimeUnit.SECONDS)) {
            //如果用户在规定时间内已经秒杀过，就返回请等待
            killService.examUser(userId);

            //若获取乐观锁，service层实现
            ItemKillSuccess map = killService.kill(killId, userId);

            return R.ok("抢购成功！");
        }else {
            //System.out.println("当前请求已被限流，直接抛弃，无法调用后续业务"+count++);
            return R.error("令牌桶未获取到 当前商品抢购过于火爆~ 请重试！");
        }
    }
//    //ItemKillSuccess killId秒杀id
//    @GetMapping("/md5/{killId}/{userId}")
//    public R getMd5(@PathVariable("killId")Integer killId, @PathVariable("userId")Integer userId){
//        String md5;
//        md5 = killService.getMd5(killId,userId);
//        return R.ok("获取md5成功").data("md5",md5);
//    }
}
