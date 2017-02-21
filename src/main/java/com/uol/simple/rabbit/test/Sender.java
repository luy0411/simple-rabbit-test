package com.uol.simple.rabbit.test;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
public class Sender {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 1000L)
    public void runA() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String date = sdf.format(new Date());
        String msg = "" + (int) (Math.random() * 10);
        // Message message = new Message(msg.getBytes(), new MessageProperties());
        //rabbitTemplate.send(RabbitConfiguration.EXCHANGE_NAME, RabbitConfiguration.ROUTING_KEY_A, msg);
        rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_NAME, RabbitConfiguration.ROUTING_KEY_A, msg);
    }


//    @Scheduled(fixedDelay = 1000L)
//    public void runB() throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
//        String date = sdf.format(new Date());
//        rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE_NAME, RabbitConfiguration.ROUTING_KEY_B, "Test B - " + date);
//    }

}