package com.zjy.rocketmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @Value("${rocketmq.producer.topic}")
    private String topic;
    @Value("${rocketmq.producer.topic1}")
    private String topic1;
    @Autowired
    private SpringProducer springProducer;

    @GetMapping("/test")
    public void test(String msg) {
//        springProducer.sendMsg(topic, "这是生产者发的消息！" + msg);
        springProducer.sendMsg1(topic1, "这是生产者发的消息！" + msg);
    }
}
