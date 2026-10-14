package dev.java.modular_monolith.product.internal.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(

        @NotEmpty(message = "O nome do produto não pode estar vazio")
        @Size(min = 2, max = 100, message = "O nome do produto só pode ter 2-100 caracteres")
        String name,

        @Size(max = 255, message = "A descrição só pode ter até 255 caracteres")
        String description,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço não pode ser menor ou igual a 0")
        BigDecimal price,

        @NotNull(message = "A quantidade em estoque é obrigatória")
        @Positive(message = "O estoque deve ser positivo")
        Integer stock

) {}
