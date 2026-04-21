package com.appsdeveloperblog.orders.service;

import com.appsdeveloperblog.core.dto.Order;
import com.appsdeveloperblog.core.types.OrderStatus;
import com.appsdeveloperblog.orders.dao.jpa.entity.OrderEntity;
import com.appsdeveloperblog.orders.dao.jpa.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Order placeOrder(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomerId(order.customerId());
        orderEntity.setProductId(order.productId());
        orderEntity.setProductQuantity(order.productQuantity());
        orderEntity.setStatus(OrderStatus.CREATED);
        orderRepository.save(orderEntity);

        return new Order(
                orderEntity.getId(),
                orderEntity.getCustomerId(),
                orderEntity.getProductId(),
                orderEntity.getProductQuantity(),
                orderEntity.getStatus());
    }

}
