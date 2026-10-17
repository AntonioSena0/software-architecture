package dev.java.hexagonal_architecture.product.domain.valueobject;

import java.math.BigDecimal;

public record Price(

        BigDecimal value

) {

    public Price {
        if(value == null || value.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalStateException("O preço não pode ser nulo ou negativo");
        }
    }

}
