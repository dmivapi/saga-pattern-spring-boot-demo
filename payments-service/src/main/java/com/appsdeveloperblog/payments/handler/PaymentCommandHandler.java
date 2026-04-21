package com.appsdeveloperblog.payments.handler;

import com.appsdeveloperblog.core.command.ProcessPaymentCommand;
import com.appsdeveloperblog.core.dto.Payment;
import com.appsdeveloperblog.core.event.PaymentFailedEvent;
import com.appsdeveloperblog.core.event.PaymentProcessedEvent;
import com.appsdeveloperblog.core.exception.CreditCardProcessorUnavailableException;
import com.appsdeveloperblog.payments.config.properties.KafkaProperties;
import com.appsdeveloperblog.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${app.kafka.payment-commands-topic-name}")
@RequiredArgsConstructor
@Slf4j
public class PaymentCommandHandler {

    private final PaymentService paymentService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    @KafkaHandler
    public void handleCommand(@Payload ProcessPaymentCommand command) {
        try {
            Payment payment = new Payment(
                    null,
                    command.orderId(),
                    command.productId(),
                    command.productPrice(),
                    command.productQuantity());

            Payment processedPayment = paymentService.process(payment);
            PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent(
                    processedPayment.orderId(),
                    processedPayment.id()
            );
            kafkaTemplate.send(kafkaProperties.paymentEventsTopicName(), paymentProcessedEvent);
        } catch (CreditCardProcessorUnavailableException e) {
            log.error(e.getLocalizedMessage(), e);
            PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(
                    command.orderId(),
                    command.productId(),
                    command.productQuantity()
            );
            kafkaTemplate.send(kafkaProperties.paymentEventsTopicName(), paymentFailedEvent);
        }
    }
}
