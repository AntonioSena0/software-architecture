package dev.java.server_sent_events.service;

import dev.java.server_sent_events.dto.request.ProductRequest;
import dev.java.server_sent_events.dto.response.ProductResponse;
import dev.java.server_sent_events.event.ProductCreatedEvent;
import dev.java.server_sent_events.event.ProductDeletedEvent;
import dev.java.server_sent_events.mapper.ProductMapper;
import dev.java.server_sent_events.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ProductMapper::toProductResponse);
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {

        ProductResponse product = ProductMapper.toProductResponse(repository.save(ProductMapper.toProduct(request)));
        eventPublisher.publishEvent(new ProductCreatedEvent(product));

        return product;

    }

    @Override
    @Transactional
    public void delete(Long id) {

        if(!repository.existsById(id)){
            throw new RuntimeException("Produto não encontrado");
        }

        repository.deleteById(id);
        eventPublisher.publishEvent(new ProductDeletedEvent(id));

    }

}