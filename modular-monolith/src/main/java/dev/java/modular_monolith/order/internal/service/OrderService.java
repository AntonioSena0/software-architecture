package dev.java.modular_monolith.order.internal.service;

import dev.java.modular_monolith.order.internal.dto.BuyRequest;

public interface OrderService {

    void buyProduct(BuyRequest request);

}
