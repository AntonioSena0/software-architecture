package dev.java.layered_architecture.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(

        @NotEmpty(message = "O nome do produto não pode estar vazio")
        @Size(max = 100, message = "O nome do produto só pode ter até 100 caracteres")
        String name,

        @Size(max = 255, message = "A descrição só pode ter até 255 caracteres")
        String description,

        @NotNull(message = "O preço é obrigatório")
        BigDecimal price,

        @NotNull(message = "A quantidade em estoque é obrigatória")
        Integer stock

) {}
