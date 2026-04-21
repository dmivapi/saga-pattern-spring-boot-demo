package com.appsdeveloperblog.payments.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ApplicationConfig {

    @Bean
    public RestClient ccpRestClient(@Value("${remote.ccp.url}") String ccpUrl) {
        return RestClient.builder()
                .baseUrl(ccpUrl)
                .build();
    }
}
