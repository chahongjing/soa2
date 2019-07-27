package com.zjy.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * TODO
 */
@Component
public class KafkaConsumer {
    @KafkaListener(topics = {"${kafka.topic}"})
    public void receive(@Payload String message, @Headers MessageHeaders headers) {
        System.out.println("KafkaMessageConsumer 接收到消息：" + message);
        headers.keySet().forEach(key -> System.out.println(key + ": " + headers.get(key)));
    }
}
