package com.appsdeveloperblog.orders.saga;

import com.appsdeveloperblog.core.command.*;
import com.appsdeveloperblog.core.event.*;
import com.appsdeveloperblog.core.type.OrderStatus;
import com.appsdeveloperblog.orders.config.properties.KafkaProperties;
import com.appsdeveloperblog.orders.service.OrderHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = {
        "${app.kafka.order-events-topic-name}",
        "${app.kafka.product-events-topic-name}",
        "${app.kafka.payment-events-topic-name}"
})
@RequiredArgsConstructor
public class OrderSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProperties kafkaProperties;
    private final OrderHistoryService orderHistoryService;

    @KafkaHandler
    public void handleEvent(@Payload OrderCreatedEvent orderCreatedEvent) {
        ReserveProductCommand reserveProductCommand = new ReserveProductCommand(
                orderCreatedEvent.orderId(),
                orderCreatedEvent.productId(),
                orderCreatedEvent.productQuantity()
        );

        kafkaTemplate.send(kafkaProperties.productCommandsTopicName(), reserveProductCommand);
        orderHistoryService.add(orderCreatedEvent.orderId(), OrderStatus.CREATED);
    }

    @KafkaHandler
    public void handleEvent(@Payload ProductReservedEvent productReservedEvent) {
        ProcessPaymentCommand processPaymentCommand = new ProcessPaymentCommand(
                productReservedEvent.orderId(),
                productReservedEvent.productId(),
                productReservedEvent.productPrice(),
                productReservedEvent.productQuantity()
        );
        kafkaTemplate.send(kafkaProperties.paymentCommandsTopicName(), processPaymentCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload PaymentProcessedEvent paymentProcessedEvent) {
        ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(paymentProcessedEvent.orderId());
        kafkaTemplate.send(kafkaProperties.orderCommandsTopicName(), approveOrderCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload OrderApprovedEvent orderApprovedEvent) {
        orderHistoryService.add(orderApprovedEvent.orderId(), OrderStatus.APPROVED);
    }

    @KafkaHandler
    public void handleEvent(@Payload PaymentFailedEvent paymentFailedEvent) {
        CancelProductReservationCommand cancelProductReservationCommand = new CancelProductReservationCommand(
                paymentFailedEvent.orderId(),
                paymentFailedEvent.productId(),
                paymentFailedEvent.productQuantity()
        );
        kafkaTemplate.send(kafkaProperties.productCommandsTopicName(), cancelProductReservationCommand);
    }

    @KafkaHandler
    public void handleEvent(@Payload ProductReservationCancelledEvent productReservationCancelledEvent) {
        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(productReservationCancelledEvent.orderId());
        kafkaTemplate.send(kafkaProperties.orderCommandsTopicName(), rejectOrderCommand);

        orderHistoryService.add(productReservationCancelledEvent.orderId(), OrderStatus.REJECTED);
    }
}
