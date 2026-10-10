package dev.java.modular_monolith.order.internal.service;

import dev.java.modular_monolith.order.OrderCreatedEvent;
import dev.java.modular_monolith.order.OrderStatus;
import dev.java.modular_monolith.order.internal.entity.Order;
import dev.java.modular_monolith.order.internal.repository.OrderRepository;
import dev.java.modular_monolith.order.internal.dto.BuyRequest;
import dev.java.modular_monolith.order.internal.exception.InsufficientStockException;
import dev.java.modular_monolith.order.internal.mapper.OrderMapper;
import dev.java.modular_monolith.product.ProductManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository repository;
    private final ProductManager productManager;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void buyProduct(BuyRequest request) {

        log.info("Iniciando processo de compra para o produto ID: {} com quantidade: {}",
                request.productId(), request.quantity());

        productManager.checkArchived(request.productId());

        Integer currentStock = productManager.getStock(request.productId());

        if(currentStock < request.quantity()){
            log.warn("Falha na compra: Estoque insuficiente para o produto ID {}. Disponível: {}, Solicitado: {}",
                    request.productId(), currentStock, request.quantity());
            throw new InsufficientStockException(
                    "Estoque insuficiente. Disponível: " + currentStock + ", Solicitado: " + request.quantity()
            );
        }

        BigDecimal unitPrice = productManager.getProductPrice(request.productId());
        BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(request.quantity()));

        Order order = repository.save(OrderMapper.toOrder(request, totalAmount, OrderStatus.CREATED));
        log.info("Pedido criado com sucesso [orderId={}]", order.getId());

        eventPublisher.publishEvent(new OrderCreatedEvent(
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        ));

    }
}
