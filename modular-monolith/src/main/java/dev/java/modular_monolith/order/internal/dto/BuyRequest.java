package dev.java.modular_monolith.order.internal.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BuyRequest(

        @NotNull(message = "O ID do produto é obrigatório")
        Long productId,

        @Min(value = 1, message = "A quantidade mínima de compra é 1")
        @Positive
        Integer quantity

) {}
