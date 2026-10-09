package dev.java.server_sent_events.controller;

import dev.java.server_sent_events.dto.request.ProductRequest;
import dev.java.server_sent_events.dto.response.ProductResponse;
import dev.java.server_sent_events.service.ProductService;
import dev.java.server_sent_events.service.ProductSseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final ProductService service;
    private final ProductSseService sseService;

    @Override
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> findAll(
            @PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Override
    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody @Valid ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestHeader(value = "Last-Event-Id", required = false) String lastEventId) {
        SseEmitter emitter =  sseService.subscribe();
        sseService.replayEvents(lastEventId);
        return emitter;
    }

}
