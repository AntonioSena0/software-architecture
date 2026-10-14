package dev.java.server_sent_events.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(

        int status,
        String message,
        LocalDateTime timestamp

) {}
