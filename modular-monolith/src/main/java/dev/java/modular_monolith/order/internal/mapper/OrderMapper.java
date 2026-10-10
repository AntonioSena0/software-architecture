package dev.java.modular_monolith.order.internal.mapper;

import dev.java.modular_monolith.order.OrderStatus;
import dev.java.modular_monolith.order.internal.entity.Order;
import dev.java.modular_monolith.order.internal.dto.BuyRequest;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class OrderMapper {

    public Order toOrder(BuyRequest request, BigDecimal totalAmount, OrderStatus status) {

        return Order
                .builder()
                .productId(request.productId())
                .quantity(request.quantity())
                .totalAmount(totalAmount)
                .status(status)
                .build();

    }

}
