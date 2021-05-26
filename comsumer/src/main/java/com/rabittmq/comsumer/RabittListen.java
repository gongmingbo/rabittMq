package com.rabittmq.comsumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import jdk.nashorn.internal.runtime.ECMAException;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName RabittListen
 * @Description TODO
 * @Author gmb
 * @Date 2021/5/25 0025 11:26
 */
@Component
public class RabittListen {
    // 通过注解自动创建 spring.simple.queue 队列
    @RabbitListener(queuesToDeclare = @Queue("spring.simple.queue"))
    public void listen(String msg) {
        System.out.println("简单队列 接收到消息：" + msg);
    }

    // 通过注解自动创建 spring.work.queue 队列
    @RabbitListener(queuesToDeclare = @Queue("spring.work.queue"))
    public void listenWork1(String msg) {
        System.out.println("work模式 接收到消息：" + msg);
    }

    // 创建两个队列共同消费
    @RabbitListener(queuesToDeclare = @Queue("spring.work.queue"))
    public void listenWork2(String msg) {
        System.out.println("work模式二 接收到消息：" + msg);
    }

    /**
     *
     * fanout
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "spring.fanout.queue", durable = "true"),
            exchange = @Exchange(
                    value = "spring.fanout.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.FANOUT
            )
    ))
    public void listenFanout1(String msg) {
        System.out.println("fanout订阅模式1 接收到消息：" + msg);
    }

    // 队列2（第二个人），同样能接收到消息
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "spring.fanout2.queue", durable = "true"),
            exchange = @Exchange(
                    value = "spring.fanout.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.FANOUT
            )
    ))
    public void listenFanout2(String msg) {
        System.out.println("fanout订阅模式2 接收到消息：" + msg);
    }


    /**
     * topic 监听
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "spring.topic.queue", durable = "true"),
            exchange = @Exchange(
                    value = "spring.topic.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"person.*"}
    ))
    public void topic(String msg) {
        System.out.println("person 接收到消息：" + msg);
    }

    // 通配规则不同，接收不到消息
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "spring.topic.queue", durable = "true"),
            exchange = @Exchange(
                    value = "spring.topic.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"money.*"}
    ))
    public void topic2(String msg) {
        System.out.println("money Student 接收到消息：" + msg);
    }
    /**
     * direct
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "spring.direct.queue", durable = "true"),
            exchange = @Exchange(
                    value = "spring.direct.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.DIRECT
            ),
            key = {"direct"}
    ))
    public void direct(String msg, Channel channel, Message message) throws Exception {
        try {
            System.out.println("小富收到消息：{}"+ msg);

            //TODO 具体业务

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        }  catch (Exception e) {

            if (message.getMessageProperties().getRedelivered()) {

                System.out.println("消息已重复处理失败,拒绝再次接收...");

                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false); // 拒绝消息
            } else {

                System.out.println("消息即将再次返回队列处理...");

                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        }
    }


    // 队列2（第二个人），key值不同，接收不到消息
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "spring.direct2.queue", durable = "true"),
            exchange = @Exchange(
                    value = "spring.direct.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.DIRECT
            ),
            key = {"direct-test"}
    ))
    public void direct2(String msg, Channel channel, Message message) throws Exception{
        try {
           System.out.println("小富收到消息：{}"+ msg);

            //TODO 具体业务

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        }  catch (Exception e) {

            if (message.getMessageProperties().getRedelivered()) {

                System.out.println("消息已重复处理失败,拒绝再次接收...");

                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false); // 拒绝消息
            } else {

                System.out.println("消息即将再次返回队列处理...");

                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        }
    }
}

