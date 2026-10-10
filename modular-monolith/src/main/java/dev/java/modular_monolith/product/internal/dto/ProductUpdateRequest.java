package dev.java.modular_monolith.product.internal.dto;

import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductUpdateRequest(

        @Size(max = 100, message = "O nome do produto só pode ter até 100 caracteres")
        String name,

        @Size(max = 255, message = "A descrição só pode ter até 255 caracteres")
        String description,

        BigDecimal price,

        Integer stock

) {}