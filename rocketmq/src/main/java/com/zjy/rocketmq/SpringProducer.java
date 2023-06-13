package com.zjy.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMsg(String topic, String msg) {
        System.out.println("发送报文：" + msg);
        this.rocketMQTemplate.convertAndSend(topic, msg);
    }

    /**
     * 发送普通消息
     */
    public void sendMsg1(String topic, String msgBody) {
        System.out.println("发送mq消息" + msgBody);
        UserInfo user = new UserInfo();
        user.setId(3L);
        user.setName(msgBody);
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(user).build());
    }

    /**
     * 发送异步消息 在SendCallback中可处理相关成功失败时的逻辑
     */
    public void sendAsyncMsg(String topic, String msgBody) {
        UserInfo user = new UserInfo();
        user.setId(3L);
        user.setName(msgBody);
        rocketMQTemplate.asyncSend(topic, MessageBuilder.withPayload(user).build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                // 处理消息发送成功逻辑
                System.out.println("消息发送成功回调！");
            }

            @Override
            public void onException(Throwable e) {
                // 处理消息发送异常逻辑
                log.error("消息发送失败回调！", e);
            }
        });
    }

    /**
     * 发送延时消息<br/>
     * 在start版本中 延时消息一共分为18个等级分别为：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h<br/>
     */
    public void sendDelayMsg(String topic, String msgBody, Integer delayLevel) {
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(msgBody).build(), 20, delayLevel);
    }

    /**
     * 发送带tag的消息,直接在topic后面加上":tag"
     */
    public void sendTagMsg(String topic, String msgBody) {
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(msgBody).build());
    }
}