package com.uol.simple.rabbit.test;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Component;

@Component
public class ReceiverA {

    public void receive(String message) throws Exception {
//        boolean isValid =  Integer.parseInt(message) % 2 == 0;
//        if (!isValid)
//            throw new AmqpRejectAndDontRequeueException("Apenas numeros pares");
//        else
            System.out.println("Received" + message);
    }

}