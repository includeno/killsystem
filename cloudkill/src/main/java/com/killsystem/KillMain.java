package com.killsystem;

import java.time.ZoneId;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author includeno
 * @since 2021/3/23 11:02
 */
@SpringBootApplication
@EnableDiscoveryClient//zookeeper
public class KillMain {

    public static void main(String[] args) {
        SpringApplication.run(KillMain.class, args);
    }

    @PostConstruct
    void started() {
        // 设置用户时区为 UTC
        //TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")));
    }

}