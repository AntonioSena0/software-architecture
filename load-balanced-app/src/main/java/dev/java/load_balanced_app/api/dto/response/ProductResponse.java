package dev.java.load_balanced_app.api.dto.response;

import dev.java.load_balanced_app.domain.enums.ProductStatus;
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
