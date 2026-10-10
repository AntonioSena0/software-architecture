package dev.java.modular_monolith.order.internal.listener;

import dev.java.modular_monolith.order.OrderCreatedEvent;
import dev.java.modular_monolith.product.ProductManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ProductStockEventListener {

    private final ProductManager productManager;

    @ApplicationModuleListener
    @Transactional
    public void onOrderCreated(OrderCreatedEvent event) {

        log.info("Processando evento OrderCreatedEvent [orderId={}, productId={}, quantity={}]",
                event.id(), event.productId(), event.quantity());

        // Para testar os eventos
//        if (true) {
//            throw new RuntimeException("Simulando queda de sistema ou erro de banco no listener!");
//        }

        productManager.decreaseStock(event.productId(), event.quantity());
        log.info("Evento processado com sucesso para o pedido ID {}", event.id());

    }

}
