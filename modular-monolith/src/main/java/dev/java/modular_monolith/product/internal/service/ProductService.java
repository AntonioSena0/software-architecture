package dev.java.modular_monolith.product.internal.service;

import dev.java.modular_monolith.product.internal.dto.ProductRequest;
import dev.java.modular_monolith.product.internal.dto.ProductResponse;
import dev.java.modular_monolith.product.internal.dto.ProductUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductResponse> findAll(Pageable pageable);
    ProductResponse findById(Long id);
    ProductResponse create(ProductRequest request);
    ProductResponse update(ProductUpdateRequest request, Long id);
    void archive(Long id);
    void desarchive(Long id);

}
