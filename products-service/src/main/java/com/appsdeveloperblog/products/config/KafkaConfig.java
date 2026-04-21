package com.appsdeveloperblog.products.config;

import com.appsdeveloperblog.products.config.properties.KafkaProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    private static final int TOPIC_PARTITIONS = 3;
    private static final int TOPIC_REPLICATION_FACTOR = 3;

    @Bean
    NewTopic productEventsTopic(KafkaProperties kafkaProperties) {
        return TopicBuilder.name(kafkaProperties.productEventsTopicName())
                .partitions(TOPIC_PARTITIONS)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .build();
    }
}
