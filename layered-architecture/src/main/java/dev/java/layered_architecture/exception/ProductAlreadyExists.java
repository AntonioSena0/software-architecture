package dev.java.layered_architecture.exception;

public class ProductAlreadyExists extends RuntimeException {
    public ProductAlreadyExists(String message) {
        super(message);
    }
}
