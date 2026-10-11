package dev.java.clean_architecture.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductUpdateRequest(

        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        String name,

        @Size(max = 255, message = "A descrição pode ter no máximo 255 caracteres")
        String description,

        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        BigDecimal price,

        @Positive(message = "O estoque deve ser positivo")
        Integer stock

) {}