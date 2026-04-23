package com.appsdeveloperblog.products.service;

import com.appsdeveloperblog.core.dto.Product;
import com.appsdeveloperblog.products.dao.jpa.entity.ProductEntity;
import com.appsdeveloperblog.products.dao.jpa.repository.ProductRepository;
import com.appsdeveloperblog.products.domain.InsufficientStock;
import com.appsdeveloperblog.products.domain.ProductNotFound;
import com.appsdeveloperblog.products.domain.ReservationOutcome;
import com.appsdeveloperblog.products.domain.Reserved;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

  @Override
  public ReservationOutcome reserve(UUID productId, int requestedQuantity) {
    Optional<ProductEntity> findByIdResult = productRepository.findById(productId);
    if (findByIdResult.isEmpty()) {
      return new ProductNotFound(productId);
    }
    ProductEntity productEntity = findByIdResult.get();

    if (requestedQuantity > productEntity.getQuantity()) {
      return new InsufficientStock(productEntity.getId(), requestedQuantity, productEntity.getQuantity());
    }

    productEntity.setQuantity(productEntity.getQuantity() - requestedQuantity);
    productRepository.save(productEntity);

    return new Reserved(
        productEntity.getId(), requestedQuantity, productEntity.getPrice());
  }

  @Override
  public void cancelReservation(UUID productId, int requestedQuantity) {
    ProductEntity productEntity = productRepository.findById(productId).orElseThrow();
    productEntity.setQuantity(productEntity.getQuantity() + requestedQuantity);
    productRepository.save(productEntity);
  }

  @Override
  public Product save(Product product) {
    ProductEntity productEntity = new ProductEntity();
    productEntity.setName(product.name());
    productEntity.setPrice(product.price());
    productEntity.setQuantity(product.quantity());
    productRepository.save(productEntity);

    return new Product(productEntity.getId(), product.name(), product.price(), product.quantity());
  }

  @Override
  public List<Product> findAll() {
    return productRepository.findAll().stream()
        .map(e -> new Product(e.getId(), e.getName(), e.getPrice(), e.getQuantity()))
        .collect(Collectors.toList());
  }
}
