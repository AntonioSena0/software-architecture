package dev.java.hexagonal_architecture.product.ports.inbound.command;

import java.math.BigDecimal;

public record UpdateProductCommand(

        String name,
        String description,
        BigDecimal price,
        Integer stock

) {}
