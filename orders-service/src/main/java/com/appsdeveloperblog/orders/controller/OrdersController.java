package com.appsdeveloperblog.orders.controller;

import com.appsdeveloperblog.core.dto.Order;
import com.appsdeveloperblog.orders.dto.CreateOrderRequest;
import com.appsdeveloperblog.orders.dto.CreateOrderResponse;
import com.appsdeveloperblog.orders.dto.OrderHistoryResponse;
import com.appsdeveloperblog.orders.service.OrderHistoryService;
import com.appsdeveloperblog.orders.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrderService orderService;
    private final OrderHistoryService orderHistoryService;


    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CreateOrderResponse placeOrder(@RequestBody @Valid CreateOrderRequest request) {
        var order = new Order(null, request.customerId(), request.productId(), request.productQuantity(), null);
        Order createdOrder = orderService.placeOrder(order);
        return new CreateOrderResponse(
                createdOrder.orderId(),
                createdOrder.customerId(),
                createdOrder.productId(),
                createdOrder.productQuantity(),
                createdOrder.status()
        );
    }

    @GetMapping("/{orderId}/history")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderHistoryResponse> getOrderHistory(@PathVariable UUID orderId) {
        return orderHistoryService.findByOrderId(orderId).stream()
                .map(h -> new OrderHistoryResponse(h.id(), h.orderId(), h.status(), h.createdAt()))
                .toList();
    }
}
