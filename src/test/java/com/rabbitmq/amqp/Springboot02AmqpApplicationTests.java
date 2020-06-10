package com.rabbitmq.amqp;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
class Springboot02AmqpApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AmqpAdmin amqpAdmin;

    /**
     * 发送消息
     * rabbitTemplate.send(exchange,routekey,messsage);
     * message需要自己构造序列化
     * 可以使用rabbitTemplate.convertAndSend(exchange,routekey,object);
     * 该方法自动序列化object，并发送
     * 接收消息
     * rabbitTemplate.receive(queues);
     * 得到的是Message格式的数据（消息头+消息体）
     * rabbitTemplate.receiveAndConvert(queues);
     * 得到的是对象（消息体转成的对象）
     */
    @Test
    void sendTest01() {
        Map<String,Object> map=new HashMap<>();
        map.put("msg","这是第一个消息");
        map.put("data", Arrays.asList("helloworld",123,true));
        //发送消息
        //对象被默认序列化后发送出去
        rabbitTemplate.convertAndSend("exchange.direct","atguigu.news",map);
    }

    @Test
    void receiveTest01() {
        //接收消息，一个接收者接受数据后，队列中就没有了
        Object obj = rabbitTemplate.receiveAndConvert("atguigu.news");
        System.out.println(obj.getClass());
        System.out.println(obj);
    }

    @Test
    void sendTest02() {
        Map<String,Object> map=new HashMap<>();
        map.put("msg","这是第二个消息");
        map.put("data", Arrays.asList("fanout",123,true));
        //发送消息
        //fanout不需要写路由键
        //对象被默认序列化后发送出去
        rabbitTemplate.convertAndSend("exchange.fanout","",map);
    }

    @Test
    void sendTest03() {
        Map<String,Object> map=new HashMap<>();
        map.put("msg","这是第三个消息");
        map.put("data", Arrays.asList("topic",123,true));
        //发送消息
        //对象被默认序列化后发送出去
        rabbitTemplate.convertAndSend("exchange.topic","test.news",map);
    }

    @Test
    void sendTest04() {
        String s="qwer";
        //发送消息
        //对象被默认序列化后发送出去
        rabbitTemplate.convertAndSend("exchange.direct","atguigu.news",s);
    }

    @Test
    public void createExchange(){
        amqpAdmin.declareExchange(new DirectExchange("exchange.adqpadmin.direct"));
        amqpAdmin.declareExchange(new FanoutExchange("exchange.adqpadmin.fanout"));
        amqpAdmin.declareExchange(new TopicExchange("exchange.adqpadmin.topic"));
    }

    @Test
    public void createQueue(){
        amqpAdmin.declareQueue(new Queue("amqp.queue",true));
    }

    //new Binding(String destination, DestinationType type, String exchange, String routekey, Map<String,Object> arguments)
    //destination是队列名
    @Test
    public void createBinding(){
        amqpAdmin.declareBinding(new Binding("amqp.queue", Binding.DestinationType.QUEUE,"exchange.adqpadmin","amqp.test",null));
    }

    @Test
    public void createBindingtopic(){
        amqpAdmin.declareBinding(new Binding("amqp.queue", Binding.DestinationType.QUEUE,"exchange.adqpadmin.topic","amqp.*",null));
    }
}
