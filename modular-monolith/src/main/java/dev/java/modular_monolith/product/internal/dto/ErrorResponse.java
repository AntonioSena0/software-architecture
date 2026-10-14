package dev.java.modular_monolith.product.internal.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(

        int status,
        String message,
        LocalDateTime timestamp

) {}
