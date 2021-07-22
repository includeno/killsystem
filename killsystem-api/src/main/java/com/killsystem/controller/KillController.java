package com.killsystem.controller;

import com.google.gson.Gson;
import com.killsystem.exception.R;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class KillController {
    @Autowired
    private LoadBalancerClient loadBalancer;

    //服务发现
    @Autowired
    DiscoveryClient discoveryClient;

    private static final OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();


    @GetMapping("/kill")
    public R kill(@RequestParam("killId") Integer killId, @RequestParam("userId") Integer userId){
        ServiceInstance serviceInstance = loadBalancer.choose("kill01");
        System.out.println("服务地址：" + serviceInstance.getUri());
        System.out.println("服务名称：" + serviceInstance.getServiceId());

        String href=serviceInstance.getUri().toString()+"/sale";

        HttpUrl.Builder httpBuilder = HttpUrl.parse(href).newBuilder();
        httpBuilder.addQueryParameter("killId",String.valueOf(killId));
        httpBuilder.addQueryParameter("userId",String.valueOf(userId));

        Request request = new Request.Builder().url(httpBuilder.build()).build();
        Response response=null;
        try {
            log.warn(request.toString());
            response = client.newCall(request).execute();
            Gson gson=new Gson();

            String responseString=response.body().string();
            log.warn("response.body():"+responseString);
            R result=gson.fromJson(String.valueOf(responseString), R.class);
            log.warn("response json"+gson.toJson(result));

            return result;
        } catch (IOException e) {
            log.warn(response.toString());
            return R.error();
        }

    }


    @GetMapping("/discovery")
    public Object discovery(String instancename){
        //获取所有的服务名称
        List<String> services=discoveryClient.getServices();
        for(String element:services){
            log.info("element: "+element);
        }
        //根据服务名称查询所有服务
        List<ServiceInstance> instances=discoveryClient.getInstances(instancename);
        for(ServiceInstance element:instances){
            log.info("instence:InstanceId "+element.getInstanceId()+" ,Host"+element.getHost()+" ,Port"+element.getPort()+" ,Scheme "+element.getScheme());
        }
        return this.discoveryClient;
    }
}
