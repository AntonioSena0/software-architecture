package dev.java.modular_monolith.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductCreatedEvent (

    Long id,
    String name,
    String description,
    BigDecimal price,
    Integer stock,
    ProductStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt

) {}
