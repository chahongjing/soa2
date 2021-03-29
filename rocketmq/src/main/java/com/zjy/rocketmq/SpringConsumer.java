package com.zjy.rocketmq;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "${rocketmq.producer.topic}", consumerGroup = "${rocketmq.producer.group}")
public class SpringConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String msg) {
//        byte[] body = msg.getBody();
//        String msg11 = new String(body);

        System.out.println("收到消息:" + msg);
    }
}
