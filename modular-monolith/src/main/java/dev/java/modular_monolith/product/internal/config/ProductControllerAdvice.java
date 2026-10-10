package dev.java.modular_monolith.product.internal.config;

import dev.java.modular_monolith.product.internal.exception.ArchivedProductException;
import dev.java.modular_monolith.product.internal.exception.ProductAlreadyExistsException;
import dev.java.modular_monolith.product.internal.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ProductControllerAdvice {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProductNotFoundException(ProductNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleProductAlreadyExists(ProductAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ArchivedProductException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleArchivedProductException(ArchivedProductException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleArgumentNotValidException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) ->
                errors.put(((FieldError) error).getField(), error.getDefaultMessage())
        );

        return errors;

    }

}
