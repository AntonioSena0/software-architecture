package dev.java.layered_architecture.config;

import dev.java.layered_architecture.dto.response.ErrorResponse;
import dev.java.layered_architecture.exception.ProductAlreadyExists;
import dev.java.layered_architecture.exception.ProductNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(ProductNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFound(ProductNotFound ex) {

        return ErrorResponse
                .builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

    }

    @ExceptionHandler(ProductAlreadyExists.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleProductAlreadyExists(ProductAlreadyExists ex) {

        return ErrorResponse
                .builder()
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleArgumentNotValidException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        });

        return errors;

    }

}
