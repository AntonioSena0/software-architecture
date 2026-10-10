package dev.java.modular_monolith.product.internal.controller;

import dev.java.modular_monolith.product.internal.dto.ProductRequest;
import dev.java.modular_monolith.product.internal.dto.ProductResponse;
import dev.java.modular_monolith.product.internal.dto.ProductUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductController {

    ResponseEntity<Page<ProductResponse>> findAll(Pageable pageable);
    ResponseEntity<ProductResponse> findById(Long id);
    ResponseEntity<ProductResponse> create(ProductRequest request);
    ResponseEntity<ProductResponse> update(ProductUpdateRequest request, Long id);
    ResponseEntity<Void> archive(Long id);

}
