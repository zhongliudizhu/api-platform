package com.winstar.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.RetriableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zl on 2018/11/19
 */
@Component
public class Product {

    private static final Logger log = LoggerFactory.getLogger(Product.class);

    public final KafkaTemplate kafkaTemplate;

    @Autowired
    public Product(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    @Transactional
    public void sendMessage(String topicName, String key, String jsonData) throws InterruptedException {
        log.info("向kafka推送订单数据:[{}]", jsonData);
        try {
            kafkaTemplate.send(topicName, key ,jsonData);
        } catch (Exception e) {
            log.error("发送数据出错！！！{}{}", topicName, jsonData);
            log.error("发送数据出错=====>", e);
        }

        //消息发送的监听器，用于回调返回信息
        kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {

            @Override
            public void onSuccess(String topic, Integer partition, String key, String value, RecordMetadata recordMetadata) {
                log.info("数据发送成功了"+value);
            }

            @Override
            public void onError(String topic, Integer partition, String key, String value, Exception exception) {
                if (exception instanceof RetriableException){
                    log.error("错误消息topic为:" + topic + ",错误消息的key为: " + key + ",错误消息的值为: " + value,exception);
                }else {
                    log.warn("错误消息topic为:" + topic + ",错误消息的key为: " + key + ",错误消息的值为: " + value,exception);
                }
            }

            @Override
            public boolean isInterestedInSuccess() {
                log.info("数据发送完毕");
                return false;
            }

        });
    }

}
