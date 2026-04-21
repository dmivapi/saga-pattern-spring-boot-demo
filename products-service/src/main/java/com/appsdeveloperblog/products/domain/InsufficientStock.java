package com.appsdeveloperblog.products.domain;

import java.util.UUID;

public record InsufficientStock(UUID productId, int requested, int available)
    implements ReservationOutcome {}
