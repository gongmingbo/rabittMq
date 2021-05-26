package com.rabiitmq.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName ConfirmCallbackService
 * @Description TODO
 * @Author gmb
 * @Date 2021/5/25 0025 14:53
 */
@Component
public class ConfirmCallbackService implements  RabbitTemplate.ConfirmCallback{
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);            //指定 ConfirmCallback

    }
    @Override
    public void confirm(CorrelationData correlationData,boolean ack, String cause) {
        if (!ack) {
            System.out.println("消息发送异常!");
        } else {
            System.out.println("发送者爸爸已经收到确认");
        }
    }
}
