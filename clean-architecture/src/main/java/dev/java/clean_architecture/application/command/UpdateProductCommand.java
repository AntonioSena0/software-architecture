package dev.java.clean_architecture.application.command;

import java.math.BigDecimal;

public record UpdateProductCommand(

        String name,
        String description,
        BigDecimal price,
        Integer stock

) {
}
