package dev.java.hexagonal_architecture.product.domain.valueobject;

public record ProductId (

        Long value

) {

    public ProductId {
        if(value == null || value <= 0) {
            throw new IllegalArgumentException("ID do produto inválido");
        }
    }

}
