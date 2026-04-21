package com.appsdeveloperblog.products.dao.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Table(name = "products")
@Entity
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private BigDecimal price;
}
