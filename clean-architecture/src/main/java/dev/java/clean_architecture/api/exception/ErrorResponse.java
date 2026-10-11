package dev.java.clean_architecture.api.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(

        int status,
        String message,
        LocalDateTime timestamp

) {}
