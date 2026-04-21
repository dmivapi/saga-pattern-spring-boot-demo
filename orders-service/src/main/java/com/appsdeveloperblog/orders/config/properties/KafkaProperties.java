package com.appsdeveloperblog.orders.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka")
public record KafkaProperties(
        String orderEventsTopicName,
        String productCommandsTopicName,
        String productEventsTopicName,
        String paymentCommandsTopicName,
        String orderCommandsTopicName
) {}
