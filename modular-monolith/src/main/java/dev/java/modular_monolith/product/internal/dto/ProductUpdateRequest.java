package dev.java.modular_monolith.product.internal.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductUpdateRequest(

        @Size(min = 2, max = 100, message = "O nome do produto só pode ter 2-100 caracteres")
        String name,

        @Size(max = 255, message = "A descrição só pode ter até 255 caracteres")
        String description,

        @DecimalMin(value = "0.01", message = "O preço não pode ser menor ou igual a 0")
        BigDecimal price,

        @Positive(message = "O estoque deve ser positivo")
        Integer stock

) {}