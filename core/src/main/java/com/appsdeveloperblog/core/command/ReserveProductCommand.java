package com.appsdeveloperblog.core.command;

import java.util.UUID;

public record ReserveProductCommand(UUID orderId, UUID productId, Integer productQuantity) {}
