package dev.java.hexagonal_architecture.product.adapters.inbound.dto.response;

public record ApiEnvelope<T>(

    String version,
    T data

) {

    public static <T> ApiEnvelope<T> of(String version, T data) {
        return new ApiEnvelope<>(version, data);
    }
}
