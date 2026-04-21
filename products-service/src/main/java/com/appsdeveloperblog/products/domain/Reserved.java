package com.appsdeveloperblog.products.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record Reserved(UUID productId, int quantity, BigDecimal price)
    implements ReservationOutcome {}
