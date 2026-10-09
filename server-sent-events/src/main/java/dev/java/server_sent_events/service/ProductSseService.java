package dev.java.server_sent_events.service;

import dev.java.server_sent_events.event.ProductCreatedEvent;
import dev.java.server_sent_events.event.ProductDeletedEvent;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ProductSseService {

    SseEmitter subscribe();
    void handleProductCreated(ProductCreatedEvent event);
    void handleProductDeleted(ProductDeletedEvent event);
    void replayEvents(String lastEventId);
    void sendHeartbeat();

}
