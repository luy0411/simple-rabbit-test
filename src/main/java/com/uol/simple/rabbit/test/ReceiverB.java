package com.uol.simple.rabbit.test;

import org.springframework.stereotype.Component;

@Component
public class ReceiverB {

    public void receive(String message) throws Exception {
        System.out.println("Received " + message);
    }

}