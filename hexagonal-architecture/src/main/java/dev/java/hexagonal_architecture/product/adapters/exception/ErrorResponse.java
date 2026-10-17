package dev.java.hexagonal_architecture.product.adapters.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(

        int status,
        String message,
        LocalDateTime timestamp

) {}
