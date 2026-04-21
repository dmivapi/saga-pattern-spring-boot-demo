package com.appsdeveloperblog.products.domain;

import java.util.UUID;

public record ProductNotFound(UUID productId) implements ReservationOutcome {}
