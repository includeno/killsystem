FROM openjdk:11.0.10-jdk

EXPOSE 8080
WORKDIR /app

#add jar file
ADD cloudkill/target/cloudkill-1.0-SNAPSHOT.jar /app/application.jar

#指定容器启动程序及参数 <ENTRYPOINT> "<CMD>"
#default port of tomcat is 8080
#ENTRYPOINT ["java","-jar","/app/application.jar","--dockerconfig=true"]
CMD java -jar /app/application.jar --server.port=8080