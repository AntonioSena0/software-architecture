package dev.java.load_balanced_app.api.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(

        @NotEmpty(message = "O nome do produto não pode estar vazio")
        @Size(max = 100, message = "O nome do produto só pode ter até 100 caracteres")
        String name,

        @Size(max = 255, message = "A descrição só pode ter até 255 caracteres")
        String description,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        BigDecimal price,

        @NotNull(message = "A quantidade em estoque é obrigatória")
        @Positive(message = "O estoque deve ser positivo")
        Integer stock

) {}
