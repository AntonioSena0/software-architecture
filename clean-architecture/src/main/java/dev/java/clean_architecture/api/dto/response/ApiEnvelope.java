package dev.java.clean_architecture.api.dto.response;

public record ApiEnvelope<T>(

    String version,
    T data

) {

    public static <T> ApiEnvelope<T> of(String version, T data) {
        return new ApiEnvelope<>(version, data);
    }
}
