package dev.java.layered_architecture.service;

import dev.java.layered_architecture.dto.request.ProductRequest;
import dev.java.layered_architecture.dto.request.ProductUpdateRequest;
import dev.java.layered_architecture.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductResponse> findAll(Pageable pageable);
    ProductResponse findById(Long id);
    ProductResponse create(ProductRequest request);
    ProductResponse update(ProductUpdateRequest request, Long id);
    void delete(Long id);

}
