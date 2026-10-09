package dev.java.server_sent_events.service;

import dev.java.server_sent_events.event.ProductCreatedEvent;
import dev.java.server_sent_events.event.ProductDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductSseServiceImpl implements ProductSseService{

    private static final int MAX_CONNECTIONS = 100;
    private static final int MAX_REPLAY = 100;
    private final Semaphore connectionSemaphore = new Semaphore(MAX_CONNECTIONS);

    private final AtomicLong eventId = new AtomicLong(0);

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final Map<Long, SseEmitter.SseEventBuilder> eventLog = new ConcurrentHashMap<>();

    @Override
    public SseEmitter subscribe() {
        if(!connectionSemaphore.tryAcquire()){
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Limite atingido");
        }

        SseEmitter emitter = new SseEmitter(0L);
        this.emitters.add(emitter);

        emitter.onCompletion(() -> {
            this.emitters.remove(emitter);
            connectionSemaphore.release();
        });
        emitter.onTimeout(() -> {
            this.emitters.remove(emitter);
            connectionSemaphore.release();
        });
        emitter.onError(e -> {
            this.emitters.remove(emitter);
            connectionSemaphore.release();
        });
        return emitter;
    }

    @Override
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductCreated(ProductCreatedEvent event) {
        buildEvent("product-created", event.product());
    }

    @Override
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductDeleted(ProductDeletedEvent event) {
        buildEvent("product-deleted", event.productId());
    }

    @Override
    @Scheduled(fixedRate = 15000)
    public void sendHeartbeat(){

        List<SseEmitter> deadEmitters = new ArrayList<>();

        for(SseEmitter emitter : this.emitters) {

            try {
                emitter.send(SseEmitter.event()
                        .comment("keep-alive"));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }

        this.emitters.removeAll(deadEmitters);

    }

    private void buildEvent(String eventName, Object data) {

        long id = eventId.incrementAndGet();
        SseEmitter.SseEventBuilder event = SseEmitter.event()
                .id(String.valueOf(id))
                .name(eventName)
                .data(data);

        eventLog.put(id, event);

        if(eventLog.size() > MAX_REPLAY){
            eventLog.remove(eventLog.keySet().iterator().next());
        }

        List<SseEmitter> deadEmitters = new ArrayList<>();
        for(SseEmitter emitter : this.emitters) {
            try {
                emitter.send(event);
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }
        this.emitters.removeAll(deadEmitters);

    }

    @Override
    public void replayEvents(String lastEventId) {
        if(lastEventId == null) return;

        List<SseEmitter> deadEmitters = new ArrayList<>();

        Long lastId = Long.parseLong(lastEventId);

        for(long i = lastId + 1; i <= eventId.get(); i++){

            SseEmitter.SseEventBuilder event = eventLog.get(i);
            if(event != null){
                for (SseEmitter emitter : emitters) {

                    try {
                        emitter.send(event);
                    } catch (IOException e){
                        deadEmitters.add(emitter);
                    }

                }
            }

        }

        this.emitters.removeAll(deadEmitters);

    }

}