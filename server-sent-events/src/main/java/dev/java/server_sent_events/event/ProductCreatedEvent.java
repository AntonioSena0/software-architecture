package dev.java.server_sent_events.event;

import dev.java.server_sent_events.dto.response.ProductResponse;

public record ProductCreatedEvent(

        ProductResponse product

) {}
