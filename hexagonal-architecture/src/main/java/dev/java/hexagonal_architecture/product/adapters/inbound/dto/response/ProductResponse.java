package dev.java.hexagonal_architecture.product.adapters.inbound.dto.response;

import dev.java.hexagonal_architecture.product.domain.enums.ProductStatus;
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
