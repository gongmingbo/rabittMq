package com.rabiitmq.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author : JCccc
 * @CreateTime : 2019/9/3
 * @Description :
 **/
@RestController
public class SendMessageController {

    @Autowired
    RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    @RequestMapping("/simple")
    public String simple(){
        String msg = "这是一个简单队列模式";
        rabbitTemplate.convertAndSend("spring.simple.queue", msg );
        return "simple";
    }
    @RequestMapping("/work")
    public String work() throws InterruptedException {
        String msg = "这是一个work模式";
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("spring.work.queue", msg + i);
        }
      return "work";
    }
    @RequestMapping("/fanout")
    public String fanout() throws InterruptedException {
        String msg = "订阅模式";
        for (int i = 0; i < 10; i++) {
            // 这里注意细节，第二个参数需要写，否则第一个参数就变成routingKey了
            rabbitTemplate.convertAndSend("spring.fanout.exchange", "", msg + i);
        }
      return "fanout";
    }
    @RequestMapping("/topic")
    public String topic() throws InterruptedException {
        rabbitTemplate.convertAndSend("spring.topic.exchange", "person.insert", "增加人员");
        rabbitTemplate.convertAndSend("spring.topic.exchange", "person.delete", "删除人员");
        rabbitTemplate.convertAndSend("spring.topic.exchange", "money.insert", "加钱");
        rabbitTemplate.convertAndSend("spring.topic.exchange", "money.delete", "减钱");
      return "topic";
    }


    /*订阅模型-Direct (路由模式)*/
   @RequestMapping("/direct")
    public String direct() throws InterruptedException {
        String msg = "路由模式";
        for (int i = 0; i < 10; i++) {
            if (i>5){
                rabbitTemplate.convertAndSend("spring.direct.exchange", "direct", msg + i);
            }else {
                rabbitTemplate.convertAndSend("spring.direct.exchange", "direct-test", msg + i);
            }
        }
      return "direct";
    }

}
