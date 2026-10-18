package dev.java.load_balanced_app.api.controller;

import dev.java.load_balanced_app.api.dto.request.ProductRequest;
import dev.java.load_balanced_app.api.dto.request.ProductUpdateRequest;
import dev.java.load_balanced_app.api.dto.response.ApiEnvelope;
import dev.java.load_balanced_app.api.dto.response.ProductResponse;
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
