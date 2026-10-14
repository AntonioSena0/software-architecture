package dev.java.layered_architecture.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(

    int status,
    String message,
    LocalDateTime timestamp

) {}
