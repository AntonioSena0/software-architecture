package dev.java.layered_architecture.controller;

import dev.java.layered_architecture.dto.request.ProductRequest;
import dev.java.layered_architecture.dto.request.ProductUpdateRequest;
import dev.java.layered_architecture.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductController {

    ResponseEntity<Page<ProductResponse>> findAll(Pageable pageable);
    ResponseEntity<ProductResponse> findById(Long id);
    ResponseEntity<ProductResponse> create(ProductRequest request);
    ResponseEntity<ProductResponse> update(ProductUpdateRequest request, Long id);
    ResponseEntity<Void> delete(Long id);

}
