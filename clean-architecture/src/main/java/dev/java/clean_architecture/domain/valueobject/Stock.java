package dev.java.clean_architecture.domain.valueobject;

public record Stock(

        Integer value

) {

    public Stock {
        if(value < 0) {
            throw new IllegalArgumentException("O estoque não pode ser negativo");
        }
    }

    public Stock decrease(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("A quantidade a decrementar deve ser maior que zero");
        }

        int newValue = value - quantity;

        if(newValue < 0) {
            throw new IllegalStateException("Estoque insuficiente");
        }

        return new Stock(newValue);

    }

}
