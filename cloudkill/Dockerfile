FROM openjdk:11.0.10-jdk

EXPOSE 8080
WORKDIR /app

ENV application_name="kill"
ENV datasource_url="jdbc:mysql://127.0.0.1:3306/miaosha?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC"
ENV datasource_username="root"
ENV datasource_password="123456"

ENV kafka_bootstrap_servers="127.0.0.1:9092"

ENV consul_host="127.0.0.1"
ENV consul_port="8500"
ENV consul_service_name="kill01"

ENV redis_host="127.0.0.1"
ENV redis_password="redis123456"
ENV redis_port="6379"
ENV redis_database="1"


#add jar file
ADD cloudkill/target/cloudkill-1.0-SNAPSHOT.jar /app/application.jar

#指定容器启动程序及参数 <ENTRYPOINT> "<CMD>"
#default port of tomcat is 8080
#ENTRYPOINT ["java","-jar","/app/application.jar","--dockerconfig=true"]
CMD java -jar /app/application.jar --server.port=8080 --spring.application.name=$application_name --spring.datasource.url=$datasource_url --spring.datasource.username=$datasource_username --spring.datasource.password=$datasource_password --spring.kafka.bootstrap-servers=$kafka_bootstrap_servers --spring.cloud.consul.host=${consul_host} --spring.cloud.consul.port=${consul_port} --spring.cloud.consul.discovery.service-name=${consul_service_name} \
    --spring.redis.host=$redis_host --spring.redis.password=$redis_password --spring.redis.port=$redis_port --spring.redis.database=$redis_database