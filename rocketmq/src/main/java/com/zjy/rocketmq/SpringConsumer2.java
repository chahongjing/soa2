package com.zjy.rocketmq;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "${rocketmq.producer.topic1}", consumerGroup = "${rocketmq.producer.group1}")
public class SpringConsumer2 implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt msg) {
        byte[] body = msg.getBody();
        String msg11 = new String(body);
        System.out.println("收到消息:" + msg11);
    }
}
