package dev.java.server_sent_events.controller;

import dev.java.server_sent_events.dto.request.ProductRequest;
import dev.java.server_sent_events.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ProductController {

    ResponseEntity<Page<ProductResponse>> findAll(Pageable pageable);
    ResponseEntity<ProductResponse> create(ProductRequest request);
    ResponseEntity<Void> delete(Long id);
    SseEmitter stream(String lastEventId);

}
