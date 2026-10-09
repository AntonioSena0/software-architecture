package dev.java.server_sent_events.service;

import dev.java.server_sent_events.dto.request.ProductRequest;
import dev.java.server_sent_events.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductResponse> findAll(Pageable pageable);
    ProductResponse create(ProductRequest request);
    void delete(Long id);

}
