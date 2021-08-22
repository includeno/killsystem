# killsystem
https://mermaid-js.github.io/mermaid/#/sequenceDiagram



# docker基础操作参考

```
#镜像
docker pull consul:1.9.7 拉取镜像
docker image rm consul:1.9.7 删除镜像

#容器
docker run consul:1.9.7 运行容器
docker start xxxx 启动容器
docker restart xxxx 重新启动容器
docker stop xxxx 停止容器
docker rm xxxx 停止容器
docker ps -a 查看运行的容器
docker logs -f xxxx 实时查看容器运行日志
docker logs xxxx 查看容器运行日志
docker logs xxxx > /root/app.log 容器运行日志导出到宿主机指定位置
```

# docker-compose基础操作参考

```
docker-compose build 重新构建镜像
docker-compose up 运行
docker-compose logs 查看运行日志
docker-compose logs xxx 查看某服务运行日志
docker-compose -f xxxx.yml logs kill > /root/kill.log 导出服务运行日志到宿主机指定位置
docker-compose stop 表示停止相关容器的运行
docker-compose rm 删除容器

```
https://docs.docker.com/compose/reference 指令参考文档
https://www.cnblogs.com/zhangzihong/p/7027566.html
https://docs.docker.com/compose/environment-variables/ 设置环境变量

kafka 参考
https://docs.spring.io/spring-kafka/reference/html/

consul 参考
https://www.cnblogs.com/xifengxiaoma/p/9798330.html

# 服务器部署步骤/Deploy steps on servers

## 拉取镜像/Images

```
docker pull consul:1.9.7
docker pull openjdk:11.0.10-jdk
docker pull zookeeper:3.5.9
docker pull wurstmeister/kafka:2.13-2.6.0
docker pull mysql:5.7.33
docker pull redis:6.0.13
```

## 环境/Environment

```
#启动环境/Start Environment
docker-compose -f docker-compose-env.yml up -d

#停止应用并清理文件/Stop And Clean Application
docker-compose -f docker-compose-env.yml stop
docker-compose -f docker-compose-env.yml down

#重新启动环境中的mysql/Restart mysql
docker-compose -f docker-compose-env.yml build
```

## 应用 使用env文件/Application with env 

```
#启动应用/Start Application
docker-compose --env-file app.env -f docker-compose-app-withenv.yml  up -d

#停止应用并清理文件/Stop And Clean Application
docker-compose -f docker-compose-app-withenv.yml stop
docker-compose -f docker-compose-app-withenv.yml down
```



已生成的docker镜像 可以选择自行构建镜像

```
docker pull includeno/killsystem-kill
docker pull includeno/killsystem-pay
docker pull includeno/killsystem-api
```

部署指令
参考command

```
command 文件 全局docker-compose部署 命令
docker-compose-env.yml是环境部署 
docker-compose-app-withenv.yml是app部署
```

/cloudkill/command 单一服务部署方法 和Dockerfile绑定
/payment/command 单一服务部署方法 和Dockerfile绑定

/api/command 单一服务部署方法 和Dockerfile绑定

# 业务用例

## 后台管理员

```mermaid
graph LR
		
    后台管理员-->添加秒杀活动 & 编辑秒杀活动 & 停用秒杀活动
    后台管理员-->添加秒杀用户
```

## 秒杀用户



```mermaid
graph LR
		
    秒杀用户-->加入秒杀活动 & 点击秒杀按钮 & 查看订单
    点击秒杀按钮-->|秒杀成功| 订单付款
    点击秒杀按钮-->|秒杀失败| 查看订单
    
```





# 订单与支付模块



## MQ订单建立

```mermaid
sequenceDiagram
    participant A as 秒杀服务
    participant J as 订单服务
    participant R as Redis
    participant M as Mysql
    A->>J: MQ秒杀成功
    J->>R: 添加redis内订单 订单状态初始化
    R->>M: 添加mysql内订单 订单状态未付款
    M->>R: 添加mysql内订单成功
    R->>J: redis内订单状态未付款 添加redis内订单成功
    
    J->>J: MQ延时消息 检查订单状态 超时时间30分钟

```

## 订单付款成功

```mermaid
sequenceDiagram
    participant User as 用户
    participant J as 订单服务
    participant R as Redis
    participant M as Mysql
    participant P as 支付服务
    User->>J: 订单付款请求
    J->>R: 检测redis内未支付订单是否存在
    R->>+M:检测MySQL内订单付款状态
    M->>+P:付款
    P->>-M:付款成功
    M->>-R:MySQL内订单付款状态改变成功
    
    J->>R: 删除redis内未支付订单，添加至支付完成订单
    R->>+M : 库存token扣除
    M->>-R : 库存token扣除成功
    R->>R : redis库存token扣除成功
    R->>J:返回订单付款成功
    J->>User: 订单付款请求成功
    
```

## 订单超时

```mermaid
sequenceDiagram
    participant J as 订单服务
    participant R as Redis
    participant M as Mysql
    J->>R: 检测未付款订单状态
    R->>J: 订单状态
    
    J->>R: 订单超时
    R->>R: 库存token还原 redis内订单超时
    R->>J: 订单状态失效
    R->>J: 订单失败


```



## 订单退款 活动生效中 未发货

```mermaid
sequenceDiagram
    participant J as 订单服务
    participant R as Redis
    participant M as Mysql
    J->>R: 检测订单状态
    R->>J: 订单状态已付款
    
    
    J->>R: 订单超时
    R->>R: 库存token还原 redis内订单超时
    R->>J: 订单状态失效
    R->>J: 订单失败
```



## 订单退款 活动未生效 未发货









# 秒杀模块



## 预热

```mermaid
sequenceDiagram
    participant A as 秒杀服务
    participant M as Mysql
    participant R as Redis
    A->>M: 读取秒杀活动信息
    M->>A: 读取秒杀活动信息成功
    A->>M: 读取秒杀活动商品信息
    M->>A: 读取秒杀活动商品信息成功
    A->>R: 秒杀活动信息&秒杀活动商品信息存入redis
    R->>A: 秒杀活动信息&秒杀活动商品信息存入redis成功
    A->>R: 新建redis用户秒杀登录信息key List结构
    A->>R: 新建redis秒杀商品库存唯一key List结构 UUID
    A->>R: 新建redis秒杀商品未支付订单信息 List结构
    A->>R: 新建redis秒杀商品已支付订单信息 List结构
    
    
```



## 秒杀

```mermaid
sequenceDiagram
		participant User as 秒杀用户
    participant A as 秒杀服务
    participant R as Redis
    participant J as 订单服务
    User->>A:加入秒杀活动
    A->>R: 读取秒杀活动信息
    R->>A: 读取秒杀活动信息成功
    A->>R: 新建redis用户秒杀登录信息key List结构
    R->>A: 新建redis用户秒杀登录信息key成功
    A->>User:加入秒杀活动成功
    
    User->>A:点击秒杀按钮
    A->>R: 前端读取秒杀活动信息
    R->>A: 前端读取秒杀活动信息成功
    A->>R: 读取redis用户秒杀登录信息
    R->>A: 读取redis用户秒杀登录信息成功
    
    A->>R: 检测/新建redis用户秒杀近10秒内登录信息 
    R->>A: 返回结果 登录次数+1 次数正常设置超时时间2分钟 次数太多超时时间不变返回失败
    
    A->>R: 获取秒杀库存唯一key
    R->>A: 秒杀获取库存成功
    A->>J: MQ通知扣库存
    A->>User:秒杀成功
    
    
    A->>R: 新建redis秒杀商品库存唯一key List结构 UUID
    A->>R: 新建redis秒杀商品未支付订单信息 List结构
    A->>R: 新建redis秒杀商品已支付订单信息 List结构
```



## 异步扣库存

