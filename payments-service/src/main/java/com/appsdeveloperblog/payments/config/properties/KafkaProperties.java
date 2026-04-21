package com.appsdeveloperblog.payments.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka")
public record KafkaProperties(String paymentCommandsTopicName, String paymentEventsTopicName) {}
