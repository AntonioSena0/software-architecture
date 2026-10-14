package dev.java.modular_monolith.order.service;

import dev.java.modular_monolith.order.OrderCreatedEvent;
import dev.java.modular_monolith.order.OrderStatus;
import dev.java.modular_monolith.order.internal.dto.BuyRequest;
import dev.java.modular_monolith.order.internal.entity.Order;
import dev.java.modular_monolith.order.internal.exception.InsufficientStockException;
import dev.java.modular_monolith.order.internal.repository.OrderRepository;
import dev.java.modular_monolith.order.internal.service.OrderServiceImpl;
import dev.java.modular_monolith.product.ProductManager;
import dev.java.modular_monolith.product.internal.exception.BusinessRuleException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private ProductManager productManager;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("Deve criar pedido com sucesso quando estoque for suficiente")
    void shouldCreateOrderSuccessfullyWhenStockIsSufficient() {
        Long productId = 1L;
        Integer quantity = 3;
        Integer currentStock = 10;
        BigDecimal unitPrice = new BigDecimal("100.00");
        BigDecimal totalAmount = new BigDecimal("300.00");

        BuyRequest request = new BuyRequest(productId, quantity);

        Order savedOrder = Order
                .builder()
                .id(1L)
                .productId(productId)
                .quantity(quantity)
                .totalAmount(totalAmount)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Mockito.doNothing().when(productManager).checkArchived(productId);
        Mockito.when(productManager.getStock(productId)).thenReturn(currentStock);
        Mockito.when(productManager.getProductPrice(productId)).thenReturn(unitPrice);
        Mockito.when(repository.save(Mockito.any(Order.class))).thenReturn(savedOrder);

        orderService.buyProduct(request);

        Mockito.verify(productManager, Mockito.times(1)).checkArchived(productId);
        Mockito.verify(productManager, Mockito.times(1)).getStock(productId);
        Mockito.verify(productManager, Mockito.times(1)).getProductPrice(productId);
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Order.class));
        Mockito.verify(eventPublisher, Mockito.times(1)).publishEvent(Mockito.any(OrderCreatedEvent.class));
    }

    @Test
    @DisplayName("Deve lancar BusinessRuleException quando produto estiver arquivado")
    void shouldThrowBusinessRuleExceptionWhenProductIsArchived() {
        Long productId = 1L;
        Integer quantity = 1;

        BuyRequest request = new BuyRequest(productId, quantity);

        Mockito.doThrow(new BusinessRuleException("Esse produto está arquivado"))
                .when(productManager).checkArchived(productId);

        Assertions.assertThrows(BusinessRuleException.class, () -> orderService.buyProduct(request));
        Mockito.verify(productManager, Mockito.never()).getStock(Mockito.anyLong());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Order.class));
        Mockito.verify(eventPublisher, Mockito.never()).publishEvent(Mockito.any(OrderCreatedEvent.class));
    }

    @Test
    @DisplayName("Deve lancar InsufficientStockException quando estoque for insuficiente")
    void shouldThrowInsufficientStockExceptionWhenStockIsInsufficient() {
        Long productId = 1L;
        Integer quantity = 10;
        Integer currentStock = 5;

        BuyRequest request = new BuyRequest(productId, quantity);

        Mockito.doNothing().when(productManager).checkArchived(productId);
        Mockito.when(productManager.getStock(productId)).thenReturn(currentStock);

        Assertions.assertThrows(InsufficientStockException.class, () -> orderService.buyProduct(request));
        Mockito.verify(productManager, Mockito.never()).getProductPrice(Mockito.anyLong());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Order.class));
        Mockito.verify(eventPublisher, Mockito.never()).publishEvent(Mockito.any(OrderCreatedEvent.class));
    }

    @Test
    @DisplayName("Deve criar pedido quando estoque for igual a quantidade solicitada")
    void shouldCreateOrderWhenStockEqualsQuantity() {
        Long productId = 1L;
        Integer quantity = 5;
        Integer currentStock = 5;
        BigDecimal unitPrice = new BigDecimal("200.00");
        BigDecimal totalAmount = new BigDecimal("1000.00");

        BuyRequest request = new BuyRequest(productId, quantity);

        Order savedOrder = Order
                .builder()
                .id(1L)
                .productId(productId)
                .quantity(quantity)
                .totalAmount(totalAmount)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Mockito.doNothing().when(productManager).checkArchived(productId);
        Mockito.when(productManager.getStock(productId)).thenReturn(currentStock);
        Mockito.when(productManager.getProductPrice(productId)).thenReturn(unitPrice);
        Mockito.when(repository.save(Mockito.any(Order.class))).thenReturn(savedOrder);

        orderService.buyProduct(request);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Order.class));
        Mockito.verify(eventPublisher, Mockito.times(1)).publishEvent(Mockito.any(OrderCreatedEvent.class));
    }

    @Test
    @DisplayName("Deve calcular valor total corretamente multiplicando preco unitario pela quantidade")
    void shouldCalculateTotalAmountCorrectlyByMultiplyingUnitPriceByQuantity() {
        Long productId = 1L;
        Integer quantity = 4;
        Integer currentStock = 10;
        BigDecimal unitPrice = new BigDecimal("50.00");
        BigDecimal expectedTotal = new BigDecimal("200.00");

        BuyRequest request = new BuyRequest(productId, quantity);

        Order savedOrder = Order
                .builder()
                .id(1L)
                .productId(productId)
                .quantity(quantity)
                .totalAmount(expectedTotal)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Mockito.doNothing().when(productManager).checkArchived(productId);
        Mockito.when(productManager.getStock(productId)).thenReturn(currentStock);
        Mockito.when(productManager.getProductPrice(productId)).thenReturn(unitPrice);
        Mockito.when(repository.save(Mockito.any(Order.class))).thenReturn(savedOrder);

        orderService.buyProduct(request);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        Mockito.verify(repository).save(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();
        Assertions.assertEquals(expectedTotal, capturedOrder.getTotalAmount());
        Assertions.assertEquals(OrderStatus.CREATED, capturedOrder.getStatus());
    }

    @Test
    @DisplayName("Deve publicar OrderCreatedEvent com dados do pedido apos criar pedido com sucesso")
    void shouldPublishOrderCreatedEventWithOrderDetailsAfterCreatingOrderSuccessfully() {
        Long productId = 1L;
        Integer quantity = 2;
        Integer currentStock = 10;
        BigDecimal unitPrice = new BigDecimal("150.00");
        BigDecimal totalAmount = new BigDecimal("300.00");

        BuyRequest request = new BuyRequest(productId, quantity);

        Order savedOrder = Order
                .builder()
                .id(1L)
                .productId(productId)
                .quantity(quantity)
                .totalAmount(totalAmount)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Mockito.doNothing().when(productManager).checkArchived(productId);
        Mockito.when(productManager.getStock(productId)).thenReturn(currentStock);
        Mockito.when(productManager.getProductPrice(productId)).thenReturn(unitPrice);
        Mockito.when(repository.save(Mockito.any(Order.class))).thenReturn(savedOrder);

        orderService.buyProduct(request);

        ArgumentCaptor<OrderCreatedEvent> eventCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
        Mockito.verify(eventPublisher).publishEvent(eventCaptor.capture());

        OrderCreatedEvent event = eventCaptor.getValue();
        Assertions.assertEquals(1L, event.id());
        Assertions.assertEquals(productId, event.productId());
        Assertions.assertEquals(quantity, event.quantity());
        Assertions.assertEquals(totalAmount, event.totalAmount());
        Assertions.assertEquals(OrderStatus.CREATED, event.orderStatus());
    }

    @Test
    @DisplayName("Deve lancar InsufficientStockException com mensagem contendo estoque disponivel e solicitado")
    void shouldThrowInsufficientStockExceptionWithMessageContainingAvailableAndRequestedStock() {
        Long productId = 1L;
        Integer quantity = 20;
        Integer currentStock = 5;

        BuyRequest request = new BuyRequest(productId, quantity);

        Mockito.doNothing().when(productManager).checkArchived(productId);
        Mockito.when(productManager.getStock(productId)).thenReturn(currentStock);

        InsufficientStockException exception = Assertions.assertThrows(
                InsufficientStockException.class,
                () -> orderService.buyProduct(request)
        );

        Assertions.assertTrue(exception.getMessage().contains("5"));
        Assertions.assertTrue(exception.getMessage().contains("20"));
    }

    @Test
    @DisplayName("Deve criar pedido com quantidade minima igual a 1")
    void shouldCreateOrderWithMinimumQuantityEqualToOne() {
        Long productId = 1L;
        Integer quantity = 1;
        Integer currentStock = 10;
        BigDecimal unitPrice = new BigDecimal("500.00");
        BigDecimal totalAmount = new BigDecimal("500.00");

        BuyRequest request = new BuyRequest(productId, quantity);

        Order savedOrder = Order
                .builder()
                .id(1L)
                .productId(productId)
                .quantity(quantity)
                .totalAmount(totalAmount)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Mockito.doNothing().when(productManager).checkArchived(productId);
        Mockito.when(productManager.getStock(productId)).thenReturn(currentStock);
        Mockito.when(productManager.getProductPrice(productId)).thenReturn(unitPrice);
        Mockito.when(repository.save(Mockito.any(Order.class))).thenReturn(savedOrder);

        orderService.buyProduct(request);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        Mockito.verify(repository).save(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();
        Assertions.assertEquals(1, capturedOrder.getQuantity());
        Assertions.assertEquals(new BigDecimal("500.00"), capturedOrder.getTotalAmount());
    }
}
