package com.appsdeveloperblog.core.event;

import com.appsdeveloperblog.core.type.ReservationFailureReason;

import java.util.UUID;

/**
 * Published when a product reservation fails.
 *
 * <p>Nullability of the quantity fields is governed by {@code reason}:
 * <ul>
 *   <li>{@link ReservationFailureReason#INSUFFICIENT_STOCK INSUFFICIENT_STOCK}
 *       — both {@code requestedQuantity} and {@code availableQuantity} are non-null.</li>
 *   <li>{@link ReservationFailureReason#PRODUCT_NOT_FOUND PRODUCT_NOT_FOUND}
 *       — both quantity fields are null.</li>
 * </ul>
 */
public record ProductReservationFailedEvent(
    UUID orderId,
    UUID productId,
    ReservationFailureReason reason,
    Integer requestedQuantity,
    Integer availableQuantity) {}
