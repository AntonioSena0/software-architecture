package dev.java.modular_monolith.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderCreatedEvent(

    Long id,
    Long productId,
    Integer quantity,
    BigDecimal totalAmount,
    OrderStatus orderStatus,
    LocalDateTime createdAt,
    LocalDateTime updatedAt

) {}
