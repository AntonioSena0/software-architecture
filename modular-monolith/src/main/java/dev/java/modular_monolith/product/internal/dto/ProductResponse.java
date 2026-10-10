package dev.java.modular_monolith.product.internal.dto;

import dev.java.modular_monolith.product.ProductStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ProductResponse(

        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        ProductStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {}
