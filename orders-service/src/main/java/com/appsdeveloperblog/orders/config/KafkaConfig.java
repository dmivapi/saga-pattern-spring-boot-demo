package com.appsdeveloperblog.orders.config;

import com.appsdeveloperblog.orders.config.properties.KafkaProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

  private static final int TOPIC_PARTITIONS = 3;
  private static final int TOPIC_REPLICATION_FACTOR = 3;

  private final KafkaProperties kafkaProperties;

  @Bean
  NewTopic orderEventsTopic() {
    return TopicBuilder.name(kafkaProperties.orderEventsTopicName())
        .partitions(TOPIC_PARTITIONS)
        .replicas(TOPIC_REPLICATION_FACTOR)
        .build();
  }

  @Bean
  NewTopic productCommandsTopic() {
    return TopicBuilder.name(kafkaProperties.productCommandsTopicName())
        .partitions(TOPIC_PARTITIONS)
        .replicas(TOPIC_REPLICATION_FACTOR)
        .build();
  }

  @Bean
  NewTopic paymentCommandsTopic() {
    return TopicBuilder.name(kafkaProperties.paymentCommandsTopicName())
            .partitions(TOPIC_PARTITIONS)
            .replicas(TOPIC_REPLICATION_FACTOR)
            .build();
  }

  @Bean
  NewTopic ordersCommandsTopic() {
    return TopicBuilder.name(kafkaProperties.orderCommandsTopicName())
            .partitions(TOPIC_PARTITIONS)
            .replicas(TOPIC_REPLICATION_FACTOR)
            .build();
  }
}
