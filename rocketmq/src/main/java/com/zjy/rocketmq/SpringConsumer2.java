package com.zjy.rocketmq;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RocketMQMessageListener(topic = "${rocketmq.producer.topic1}", consumerGroup = "${rocketmq.producer.group1}")
public class SpringConsumer2 implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt msg) {
        byte[] body = msg.getBody();
        String msg11 = new String(body, StandardCharsets.UTF_8);
        UserInfo userInfo = JSON.parseObject(msg11, UserInfo.class);
        System.out.println("收到消息UserInfo:" + JSON.toJSONString(userInfo));
    }
}
