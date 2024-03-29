package com.zjy.rocketmq;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "${rocketmq.producer.topic}", consumerGroup = "${rocketmq.producer.group}")
//@RocketMQMessageListener(topic = "${rocketmq.producer.topic}", consumerGroup = "${rocketmq.producer.group}",
//        selectorExpression = "${kfs.rocketmq.org-update.tag}")
public class SpringConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String msg) {
        System.out.println("收到消息:" + msg);
    }
}
