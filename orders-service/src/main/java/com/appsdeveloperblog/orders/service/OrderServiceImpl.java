package com.appsdeveloperblog.orders.service;

import com.appsdeveloperblog.core.dto.Order;
import com.appsdeveloperblog.core.event.OrderApprovedEvent;
import com.appsdeveloperblog.core.event.OrderCreatedEvent;
import com.appsdeveloperblog.core.type.OrderStatus;
import com.appsdeveloperblog.orders.dao.jpa.entity.OrderEntity;
import com.appsdeveloperblog.orders.dao.jpa.repository.OrderRepository;
import com.appsdeveloperblog.orders.config.properties.KafkaProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    @Override
    public Order placeOrder(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomerId(order.customerId());
        orderEntity.setProductId(order.productId());
        orderEntity.setProductQuantity(order.productQuantity());
        orderEntity.setStatus(OrderStatus.CREATED);
        orderRepository.save(orderEntity);

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                orderEntity.getId(),
                orderEntity.getCustomerId(),
                orderEntity.getProductId(),
                orderEntity.getProductQuantity()
        );

        kafkaTemplate.send(kafkaProperties.orderEventsTopicName(), orderCreatedEvent);

        return new Order(
                orderEntity.getId(),
                orderEntity.getCustomerId(),
                orderEntity.getProductId(),
                orderEntity.getProductQuantity(),
                orderEntity.getStatus());
    }

    @Override
    public void approveOrder(UUID uuid) {
        orderRepository.findById(uuid).ifPresent(
            orderEntity -> {
                orderEntity.setStatus(OrderStatus.APPROVED);
                orderRepository.save(orderEntity);

                OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(uuid);
                kafkaTemplate.send(kafkaProperties.orderEventsTopicName(), orderApprovedEvent);
            }
        );
    }

    @Override
    public void rejectOrder(UUID uuid) {
        orderRepository.findById(uuid).ifPresent(
                orderEntity -> {
                    orderEntity.setStatus(OrderStatus.REJECTED);
                    orderRepository.save(orderEntity);
                });
    }
}
