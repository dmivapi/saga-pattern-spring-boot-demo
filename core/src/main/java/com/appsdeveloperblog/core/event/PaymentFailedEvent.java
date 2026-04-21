package com.appsdeveloperblog.core.event;

import java.util.UUID;

public record PaymentFailedEvent(
        UUID orderId,
        UUID productId,
        Integer productQuantity
) {}
