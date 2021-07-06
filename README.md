# killsystem
my personal springcloud demo

分支说明:

version1 version_1_init 

初始项目,只配置好父工程和初始服务 消费者服务和订单服务

version2 version_2_eureka_service

单机版 使用eureka进行基础性协调服务,使用actuator

按照服务器ip-服务名-端口号展示,添加actuator

version2.1 version_2_eureka_service_discovery

version3 
基础集群版 eureka集群 服务发现且消费者服务关联eureka内订单服务
eureka 2个端口7000和7001 eureka服务器相互注册
消费者服务 2个端口5000 5001 仅变化端口
订单服务2个端口8000 8001 仅变化端口
Bean DiscoveryClient

version3.1关闭eureka自我保护机制



version4
使用zookeeper替代eureka作为服务注册中心



version5 consul



version gateway



version6 nacos



python with springcloud eureka

```
https://pypi.org/project/py-eureka-client/
参考 https://www.jianshu.com/p/252411b62bea
```

