package com.appsdeveloperblog.orders.dto;

import com.appsdeveloperblog.core.type.OrderStatus;

import java.util.UUID;

public record CreateOrderResponse(
        UUID orderId,
        UUID customerId,
        UUID productId,
        Integer productQuantity,
        OrderStatus status
) {}
