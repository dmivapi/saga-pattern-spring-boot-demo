package com.appsdeveloperblog.products.handler;

import com.appsdeveloperblog.core.command.ReserveProductCommand;
import com.appsdeveloperblog.core.event.ProductReservationFailedEvent;
import com.appsdeveloperblog.core.event.ProductReservedEvent;
import com.appsdeveloperblog.core.type.ReservationFailureReason;
import com.appsdeveloperblog.products.config.properties.KafkaProperties;
import com.appsdeveloperblog.products.domain.InsufficientStock;
import com.appsdeveloperblog.products.domain.ProductNotFound;
import com.appsdeveloperblog.products.domain.ReservationOutcome;
import com.appsdeveloperblog.products.domain.Reserved;
import com.appsdeveloperblog.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@KafkaListener(topics="${app.kafka.product-commands-topic-name}")
@Slf4j
@RequiredArgsConstructor
public class ProductCommandHandler {

    private final ProductService productService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

  @KafkaHandler
  public void handleCommand(@Payload ReserveProductCommand command) {
    try(var _ = MDC.putCloseable("orderId", command.orderId().toString())) {
      ReservationOutcome outcome = productService.reserve(command.productId(), command.productQuantity());
      switch (outcome) {
        case Reserved           r -> publishReserved(command.orderId(), r);
        case InsufficientStock  s -> publishReservationFailed(command.orderId(), s);
        case ProductNotFound    n -> publishReservationFailed(command.orderId(), n);
      }
    }
  }

    private void publishReserved(UUID orderId, Reserved reserved) {
        log.info("Product reserved. orderId={}, productId={}, quantity={}", orderId, reserved.productId(), reserved.quantity());
        kafkaTemplate.send(
                kafkaProperties.productEventsTopicName(),
                new ProductReservedEvent(orderId, reserved.productId(), reserved.price(), reserved.quantity()));
    }

    private void publishReservationFailed(UUID orderId, InsufficientStock s) {
        log.warn("Reservation failed (insufficient stock). orderId: {}, productId: {}, requested: {}, available: {}", orderId, s.productId(), s.requested(), s.available());
        kafkaTemplate.send(
                kafkaProperties.productEventsTopicName(),
                new ProductReservationFailedEvent(orderId, s.productId(),
                        ReservationFailureReason.INSUFFICIENT_STOCK,
                        s.requested(), s.available()));
    }

    private void publishReservationFailed(UUID orderId, ProductNotFound n) {
        log.warn("Reservation failed (product not found). orderId: {}, productId: {}", orderId, n.productId());
        kafkaTemplate.send(
                kafkaProperties.productEventsTopicName(),
                new ProductReservationFailedEvent(orderId, n.productId(),
                        ReservationFailureReason.PRODUCT_NOT_FOUND,
                        null, null));
    }
}
