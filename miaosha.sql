CREATE DATABASE `miaosha` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE miaosha;
CREATE TABLE `item`
(
    `id`            int(11) NOT NULL AUTO_INCREMENT,
    `name`          varchar(45) DEFAULT NULL,
    `code`          varchar(45) DEFAULT NULL,
    `stock`         bigint(30) DEFAULT NULL,
    `purchase_time` varchar(45) DEFAULT NULL,
    `version`       varchar(45) DEFAULT NULL,
    `is_active`     varchar(45) DEFAULT NULL COMMENT '1有效 0无效',
    `create_time`   datetime    DEFAULT NULL,
    `update_time`   datetime    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `item_kill`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `item_id`     varchar(45) DEFAULT NULL COMMENT '商品id',
    `total`       varchar(45) DEFAULT NULL COMMENT '可被秒杀的总数',
    `version`     int(11) DEFAULT NULL COMMENT '乐观锁',
    `start_time`  datetime    DEFAULT NULL COMMENT '秒杀开始时间',
    `end_time`    datetime    DEFAULT NULL COMMENT '秒杀结束时间',
    `is_active`   varchar(45) DEFAULT NULL COMMENT '是否有效（1=是；0=否）',
    `create_time` datetime    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='待秒杀商品表';

CREATE TABLE `item_kill_success`
(
    `id`          varchar(50) NOT NULL COMMENT '秒杀成功生成的订单编号',
    `item_id`     int(11) DEFAULT NULL COMMENT '商品id',
    `kill_id`     int(11) DEFAULT NULL COMMENT '秒杀id',
    `user_id`     int(11) DEFAULT NULL COMMENT '用户id',
    `status`      int(11) DEFAULT NULL COMMENT '秒杀结果: -1无效  0成功(未付款)  1已付款  2已取消',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `randomcode`
(
    `id`   int(11) NOT NULL,
    `code` varchar(64) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用于雪花算法生成id';

CREATE TABLE `user`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `username`   varchar(45) DEFAULT NULL COMMENT '用户名',
    `password`    varchar(45) DEFAULT NULL COMMENT '密码',
    `phone`       varchar(45) DEFAULT NULL COMMENT '手机号',
    `email`       varchar(45) DEFAULT NULL COMMENT '邮箱',
    `is_hidden`   int(11) DEFAULT NULL COMMENT '是否隐藏(1=是；0=否)',
    `is_active`   int(11) DEFAULT NULL COMMENT '是否有效(1=是；0=否)',
    `money` int(11) DEFAULT '0' COMMENT '金额',
    `create_time` datetime    DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime    DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

CREATE TABLE `userrole`
(
    `id`        int(11) NOT NULL,
    `user_id`   varchar(45) DEFAULT NULL,
    `role_id`   varchar(45) DEFAULT NULL,
    `is_active` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;