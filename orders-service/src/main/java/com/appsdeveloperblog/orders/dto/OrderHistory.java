package com.appsdeveloperblog.orders.dto;

import com.appsdeveloperblog.core.type.OrderStatus;

import java.sql.Timestamp;
import java.util.UUID;

public record OrderHistory(
        UUID id,
        UUID orderId,
        OrderStatus status,
        Timestamp createdAt
) {}
