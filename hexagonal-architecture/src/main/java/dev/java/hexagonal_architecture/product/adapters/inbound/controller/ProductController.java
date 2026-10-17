package dev.java.hexagonal_architecture.product.adapters.inbound.controller;

import dev.java.hexagonal_architecture.product.adapters.inbound.dto.request.ProductRequest;
import dev.java.hexagonal_architecture.product.adapters.inbound.dto.request.ProductUpdateRequest;
import dev.java.hexagonal_architecture.product.adapters.inbound.dto.response.ApiEnvelope;
import dev.java.hexagonal_architecture.product.adapters.inbound.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface ProductController {

    ResponseEntity<ApiEnvelope<Page<ProductResponse>>> findAll(Pageable pageable);
    ResponseEntity<ApiEnvelope<ProductResponse>> findById(Long id);
    ResponseEntity<ApiEnvelope<ProductResponse>> create(ProductRequest request);
    ResponseEntity<ApiEnvelope<ProductResponse>> update(ProductUpdateRequest request, Long id);
    ResponseEntity<Void> archive(Long id);
    ResponseEntity<Void> desarchive(Long id);

}
