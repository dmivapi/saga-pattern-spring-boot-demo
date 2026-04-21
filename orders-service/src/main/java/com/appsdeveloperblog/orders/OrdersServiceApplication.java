package com.appsdeveloperblog.orders;

import com.appsdeveloperblog.orders.config.properties.KafkaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(KafkaProperties.class)
public class OrdersServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(OrdersServiceApplication.class, args);
    }
}
