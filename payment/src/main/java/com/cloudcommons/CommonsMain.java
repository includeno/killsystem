package com.cloudcommons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author includeno
 * @since 2021/3/23 11:02
 */
@SpringBootApplication
//@EnableDiscoveryClient//zookeeper
public class CommonsMain {

    public static void main(String[] args) {
        SpringApplication.run(CommonsMain.class, args);
    }

}