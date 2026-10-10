package dev.java.modular_monolith.order.internal.controller;

import dev.java.modular_monolith.order.internal.dto.BuyRequest;
import org.springframework.http.ResponseEntity;

public interface OrderController {

    ResponseEntity<Void> buy(BuyRequest request);

}
