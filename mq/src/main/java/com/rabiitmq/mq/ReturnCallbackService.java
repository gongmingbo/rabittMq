package com.rabiitmq.mq;

import com.rabbitmq.client.Return;
import com.rabbitmq.client.ReturnCallback;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName ReturnCallbackService
 * @Description TODO
 * @Author gmb
 * @Date 2021/5/25 0025 14:56
 */
@Component
public class ReturnCallbackService implements RabbitTemplate.ReturnCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setReturnCallback(this);             //指定 ReturnCallback
    }
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        System.out.println("回退");
    }
}
