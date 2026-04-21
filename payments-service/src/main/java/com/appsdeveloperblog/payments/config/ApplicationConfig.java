package com.appsdeveloperblog.payments.config;

import com.appsdeveloperblog.payments.config.properties.KafkaProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class ApplicationConfig {

    @Bean
    public RestClient ccpRestClient(@Value("${remote.ccp.url}") String ccpUrl) {
        return RestClient.builder()
                .baseUrl(ccpUrl)
                .build();
    }
}
