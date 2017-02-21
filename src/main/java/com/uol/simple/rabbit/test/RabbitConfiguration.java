package com.uol.simple.rabbit.test;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
class RabbitConfiguration {

    public static final String EXCHANGE_NAME = "simple-rabbit-boot-test-exchange";
    public static final String QUEUE_A_NAME = "simple-rabbit-boot-test-queue-a";
    public static final String QUEUE_B_NAME = "simple-rabbit-boot-test-queue-b";
    public static final String ROUTING_KEY_A = "routing-key-a";
    public static final String ROUTING_KEY_B = "routing-key-b";

    public static final String DEAD_EXCHANGE_NAME = "simple-rabbit-boot-test-dead-exchange";
    public static final String DEAD_QUEUE_NAME = "simple-rabbit-boot-test-dead-queue";

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("a2-humbrella1");
        cachingConnectionFactory.setUsername("simple-rabbit-app");
        cachingConnectionFactory.setPassword("simple-rabbit-app");
        cachingConnectionFactory.setPort(5672);
        return cachingConnectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public FanoutExchange deadExchange() {
        return new FanoutExchange(DEAD_EXCHANGE_NAME);
    }

    @Bean
    public Queue queueA() {
        return QueueBuilder.durable(QUEUE_A_NAME)
                           .withArgument("x-dead-letter-exchange", DEAD_EXCHANGE_NAME)
                           .withArgument("x-dead-letter-routing-key", DEAD_QUEUE_NAME)
                           .build();
    }

    @Bean
    public Queue queueB() {
        return QueueBuilder.durable(QUEUE_B_NAME)
                           .withArgument("x-dead-letter-exchange", DEAD_EXCHANGE_NAME)
                           .withArgument("x-dead-letter-routing-key", DEAD_QUEUE_NAME)
                           .build();
    }

    @Bean
    public Queue deadQueue() {
        Map<String, Object> arguments = new HashMap();
        arguments.put("x-message-ttl", 5000);
        return QueueBuilder.durable(DEAD_QUEUE_NAME)
                           .withArguments(arguments)
                           .build();
    }

    @Bean
    public Binding binding_queue_a(Queue queueA, DirectExchange exchange) {
        return BindingBuilder.bind(queueA).to(exchange).with(ROUTING_KEY_A);
    }

    @Bean
    public Binding binding_queue_b(Queue queueB, DirectExchange exchange) {
        return BindingBuilder.bind(queueB).to(exchange).with(ROUTING_KEY_B);
    }

    @Bean
    public Binding binding_dead_queue(Queue deadQueue, FanoutExchange deadExchange) {
        return BindingBuilder.bind(deadQueue).to(deadExchange);
    }

    @Bean
    public ReceiverA receiverA() {
        return new ReceiverA();
    }

    @Bean
    public SimpleMessageListenerContainer containerA(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapterA) {
        SimpleMessageListenerContainer container = getSimpleMessageListenerContainer(connectionFactory, listenerAdapterA, QUEUE_A_NAME);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapterA(ReceiverA receiverA) {
        return new MessageListenerAdapter(receiverA, "receive");
    }

    @Bean
    public ReceiverB receiverB() {
        return new ReceiverB();
    }

    @Bean
    public SimpleMessageListenerContainer containerB(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapterB) {
        SimpleMessageListenerContainer container = getSimpleMessageListenerContainer(connectionFactory, listenerAdapterB, QUEUE_B_NAME);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapterB(ReceiverB receiverB) {
        return new MessageListenerAdapter(receiverB, "receive");
    }

    private SimpleMessageListenerContainer getSimpleMessageListenerContainer(ConnectionFactory connectionFactory,
                                                                             MessageListenerAdapter listenerAdapterA,
                                                                             String queueName) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapterA);
        container.setMissingQueuesFatal(false);

        return container;
    }

}
