package com.appsdeveloperblog.core.event;

import java.util.UUID;

public record ProductReservationCancelledEvent(UUID orderId, UUID productId) {}
