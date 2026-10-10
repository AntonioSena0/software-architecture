package dev.java.modular_monolith.order.internal.controller;

import dev.java.modular_monolith.order.internal.service.OrderService;
import dev.java.modular_monolith.order.internal.dto.BuyRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService service;

    @Override
    @PostMapping("/buy")
    public ResponseEntity<Void> buy(@RequestBody @Valid BuyRequest request) {
        service.buyProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
