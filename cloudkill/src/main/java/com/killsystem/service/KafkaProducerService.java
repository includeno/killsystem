package com.killsystem.service;

import com.killsystem.config.KafkaTopic;
import com.killsystem.entity.ItemKillSuccess;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@Service
public class KafkaProducerService {
    public Logger log= LoggerFactory.getLogger("KafkaProducerService");
    @Autowired
    @Qualifier("kafkaTemplate")
    KafkaTemplate<Integer,String> kafkaTemplate;

    @Autowired
    Gson gson;

    public ListenableFuture<SendResult<Integer, String>> sendAsync(ItemKillSuccess message){

        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(KafkaTopic.itemKillSuccess, gson.toJson(message,ItemKillSuccess.class));

        future.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                log.warn("mq itemKillSuccess succeed:"+gson.toJson(message,ItemKillSuccess.class));;
            }

            @Override
            public void onFailure(Throwable ex) {
                log.warn("mq itemKillSuccess failed:"+gson.toJson(message,ItemKillSuccess.class)+" reason:"+ex.getMessage());;
            }

        });

        return future;
    }

    public Boolean sendSync(ItemKillSuccess message){
        ProducerRecord<String, String> record =new ProducerRecord<>(KafkaTopic.itemKillSuccess,gson.toJson(message,ItemKillSuccess.class));

        try {
            kafkaTemplate.send((Message<?>) record).get(10, TimeUnit.SECONDS);
            return Boolean.TRUE;
        }
        catch (ExecutionException e) {
            //handleFailure(data, record, e.getCause());
            log.error(e.getMessage());
            return Boolean.FALSE;
        }
        catch (TimeoutException | InterruptedException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        finally {

        }
    }



}
