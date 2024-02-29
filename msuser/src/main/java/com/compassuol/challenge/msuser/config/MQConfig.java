package com.compassuol.challenge.msuser.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    @Value("${mq.queues.notifications}")
    private String notificationsQueue;

    @Bean
    public Queue queueNotifications() {
        return new Queue(notificationsQueue, true);
    }

}
