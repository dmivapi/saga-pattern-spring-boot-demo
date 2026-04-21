package com.appsdeveloperblog.payments.service;

import com.appsdeveloperblog.core.dto.CreditCardProcessRequest;
import com.appsdeveloperblog.core.exception.CreditCardProcessorUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class CreditCardProcessorRemoteServiceImpl implements CreditCardProcessorRemoteService {
    private final RestClient restClient;

    @Override
    public void process(BigInteger cardNumber, BigDecimal paymentAmount) {
        try {
            var request = new CreditCardProcessRequest(cardNumber, paymentAmount);
            restClient.post()
                    .uri("/ccp/process")
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();
        } catch (ResourceAccessException e) {
            throw new CreditCardProcessorUnavailableException(e);
        }
    }
}
