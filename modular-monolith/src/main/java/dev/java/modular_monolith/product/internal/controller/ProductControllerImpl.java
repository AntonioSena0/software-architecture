package dev.java.modular_monolith.product.internal.controller;

import dev.java.modular_monolith.product.internal.dto.ProductRequest;
import dev.java.modular_monolith.product.internal.dto.ProductResponse;
import dev.java.modular_monolith.product.internal.dto.ProductUpdateRequest;
import dev.java.modular_monolith.product.internal.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductControllerImpl implements ProductController{

    private final ProductService service;

    @Override
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> findAll(
            @PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody @Valid ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@RequestBody @Valid ProductUpdateRequest request, @PathVariable Long id) {
        return ResponseEntity.ok(service.update(request, id));
    }

    @Override
    @PatchMapping("/archive/{id}")
    public ResponseEntity<Void> archive(@PathVariable Long id) {
        service.archive(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/desarchive/{id}")
    public ResponseEntity<Void> desarchive(@PathVariable Long id) {
        service.desarchive(id);
        return ResponseEntity.noContent().build();
    }

}
