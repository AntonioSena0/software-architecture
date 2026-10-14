package dev.java.modular_monolith.order.listener;

import dev.java.modular_monolith.order.OrderCreatedEvent;
import dev.java.modular_monolith.order.OrderStatus;
import dev.java.modular_monolith.product.ProductManager;
import dev.java.modular_monolith.order.internal.listener.ProductStockEventListener;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class ProductStockEventListenerTest {

    @Mock
    private ProductManager productManager;

    @InjectMocks
    private ProductStockEventListener listener;

    @Test
    @DisplayName("Deve diminuir estoque do produto quando OrderCreatedEvent for recebido")
    void shouldDecreaseProductStockWhenOrderCreatedEventIsReceived() {
        Long orderId = 1L;
        Long productId = 10L;
        Integer quantity = 5;
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, productId, quantity, new BigDecimal("499.95"), OrderStatus.CREATED, LocalDateTime.now(), LocalDateTime.now());

        listener.onOrderCreated(event);

        Mockito.verify(productManager, Mockito.times(1)).decreaseStock(productId, quantity);
    }

    @Test
    @DisplayName("Deve diminuir estoque com quantidade correta quando evento tiver quantidade diferente de um")
    void shouldDecreaseStockWithCorrectQuantityWhenEventHasQuantityDifferentFromOne() {
        Long orderId = 2L;
        Long productId = 20L;
        Integer quantity = 10;
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, productId, quantity, new BigDecimal("999.90"), OrderStatus.CREATED, LocalDateTime.now(), LocalDateTime.now());

        listener.onOrderCreated(event);

        Mockito.verify(productManager, Mockito.times(1)).decreaseStock(productId, quantity);
    }

    @Test
    @DisplayName("Deve chamar decreaseStock com produto correto quando evento tiver produto especifico")
    void shouldCallDecreaseStockWithCorrectProductWhenEventHasSpecificProduct() {
        Long orderId = 3L;
        Long productId = 30L;
        Integer quantity = 2;
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, productId, quantity, new BigDecimal("199.98"), OrderStatus.CREATED, LocalDateTime.now(), LocalDateTime.now());

        listener.onOrderCreated(event);

        Mockito.verify(productManager, Mockito.times(1)).decreaseStock(productId, quantity);
        Mockito.verify(productManager, Mockito.never()).decreaseStock(Mockito.eq(999L), Mockito.anyInt());
    }
}
