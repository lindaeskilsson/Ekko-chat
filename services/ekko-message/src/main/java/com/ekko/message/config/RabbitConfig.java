package com.ekko.message.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String MESSAGE_PUBLISHED = "message-published";

    @Bean
    public Queue messagePublishedQueue() {
        return new Queue(MESSAGE_PUBLISHED, true);
    }
}