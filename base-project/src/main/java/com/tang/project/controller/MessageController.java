//package com.tang.project.controller;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.concurrent.ThreadLocalRandom;
//import java.util.stream.IntStream;
//
///**
// * 异步处理消息中心demo
// */
//@RestController
//@Slf4j
//@RequestMapping("message")
//public class MessageController {
//
//    //        @Autowired
////        private UserService userService;
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    @GetMapping("register")
//    public void register() {
//        //模拟10个用户注册
//        IntStream.rangeClosed(1, 10).forEach(i -> {
//            //落库
////                User user = userService.register();
//            //模拟50%的消息可能发送失败
//            if (ThreadLocalRandom.current().nextInt(10) % 2 == 0) {
//                //通过RabbitMQ发送消息
//                rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE, RabbitConfiguration.ROUTING_KEY, user);
//                log.info("sent mq user {}", user.getId());
//            }
//        });
//    }
//
//
//}
