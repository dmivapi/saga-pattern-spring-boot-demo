package com.appsdeveloperblog.orders.handler;

import com.appsdeveloperblog.core.command.ApproveOrderCommand;
import com.appsdeveloperblog.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "${app.kafka.order-commands-topic-name}")
@RequiredArgsConstructor
public class OrderCommandHandler {

    private final OrderService orderService;

    @KafkaHandler
    public void handler(@Payload ApproveOrderCommand approveOrderCommand) {
        orderService.approveOrder(approveOrderCommand.orderId());
    }
}
