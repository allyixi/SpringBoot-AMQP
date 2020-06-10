package com.rabbitmq.amqp.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TestService {

//    @RabbitListener(queues = "atguigu.news")
//    public void receive01(Map map){
//        System.out.println("收到消息"+map);
//    }

    @RabbitListener(queues = "atguigu.news")
    public void receive02(Message msg){
        System.out.println(msg.getBody());
        System.out.println(msg.getMessageProperties());
    }
}
