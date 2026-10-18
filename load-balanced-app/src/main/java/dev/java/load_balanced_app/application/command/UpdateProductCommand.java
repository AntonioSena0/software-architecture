package dev.java.load_balanced_app.application.command;

import java.math.BigDecimal;

public record UpdateProductCommand(

        String name,
        String description,
        BigDecimal price,
        Integer stock

) {
}
