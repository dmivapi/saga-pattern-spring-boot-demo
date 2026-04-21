package com.appsdeveloperblog.products.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka")
public record KafkaProperties(
        String productCommandsTopicName,
        String productEventsTopicName
) {}
