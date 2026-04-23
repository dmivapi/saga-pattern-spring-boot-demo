package com.appsdeveloperblog.core.command;

import java.util.UUID;

public record RejectOrderCommand(UUID orderId) {}
