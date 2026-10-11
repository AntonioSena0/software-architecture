package dev.java.clean_architecture.domain.valueobject;

public record ProductId (

        Long value

) {

    public ProductId {
        if(value == null || value <= 0) {
            throw new IllegalArgumentException("ID do produto inválido");
        }
    }

}
