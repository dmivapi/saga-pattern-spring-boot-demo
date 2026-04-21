package com.appsdeveloperblog.products.service;

import com.appsdeveloperblog.core.dto.Product;
import com.appsdeveloperblog.products.domain.ReservationOutcome;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> findAll();
    ReservationOutcome reserve(UUID productId, int requestedQuantity);
    void cancelReservation(Product productToCancel, UUID orderId);
    Product save(Product product);
}
