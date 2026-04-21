package com.appsdeveloperblog.payments.config;

import com.appsdeveloperblog.payments.config.properties.KafkaProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

    private static final int TOPIC_PARTITIONS = 3;
    private static final int TOPIC_REPLICATION_FACTOR = 3;

    @Bean
    NewTopic paymentEventsTopic(KafkaProperties kafkaProperties) {
        return TopicBuilder.name(kafkaProperties.paymentEventsTopicName())
                .partitions(TOPIC_PARTITIONS)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .build();
    }
}
