package com.appsdeveloperblog.products.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreationResponse {
    private UUID id;
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
