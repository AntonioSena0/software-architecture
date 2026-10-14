package dev.java.modular_monolith.product.internal.service;

import dev.java.modular_monolith.product.ProductCreatedEvent;
import dev.java.modular_monolith.product.ProductManager;
import dev.java.modular_monolith.product.ProductStatus;
import dev.java.modular_monolith.product.internal.entity.Product;
import dev.java.modular_monolith.product.internal.repository.ProductRepository;
import dev.java.modular_monolith.product.internal.dto.ProductRequest;
import dev.java.modular_monolith.product.internal.dto.ProductResponse;
import dev.java.modular_monolith.product.internal.dto.ProductUpdateRequest;
import dev.java.modular_monolith.product.internal.exception.BusinessRuleException;
import dev.java.modular_monolith.product.internal.exception.ProductAlreadyExistsException;
import dev.java.modular_monolith.product.internal.exception.ProductNotFoundException;
import dev.java.modular_monolith.product.internal.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService, ProductManager {

    private final ProductRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {
        log.debug("Buscando lista paginada de produtos [page={}, size={}]", pageable.getPageNumber(), pageable.getPageSize());
        return repository.findAll(pageable)
                .map(ProductMapper::toProductResponse);
    }

    @Override
    public ProductResponse findById(Long id) {

        log.debug("Buscando produto por ID: {}", id);
        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tentativa de buscar produto inexistente com ID: {}", id);
                    return new ProductNotFoundException("Produto não encontrado");
                });

        return ProductMapper.toProductResponse(product);

    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {

        log.info("Iniciando cadastro de novo produto: '{}'", request.name());

        if(repository.existsByName(request.name().trim().toLowerCase().replaceAll("\\s+", " "))){
            throw new ProductAlreadyExistsException("Esse produto já está cadastrado");
        }

        ProductStatus status = ProductStatus.ACTIVE;

        if(request.stock() <= 0){
            status = ProductStatus.INACTIVE;
            log.info("Estoque inicial menor ou igual a zero. Produto '{}' será criado como INACTIVE.", request.name());
        }

        Product created = repository.save(ProductMapper.toProduct(request, status));
        log.info("Produto cadastrado com sucesso [productId={}, status={}]", created.getId(), created.getStatus());

        ProductResponse product = ProductMapper.toProductResponse(created);

        eventPublisher.publishEvent(new ProductCreatedEvent(
                product.id(),
                product.name(),
                product.description(),
                product.price(),
                product.stock(),
                product.status(),
                product.createdAt(),
                product.updatedAt()
        ));

        return product;
    }

    @Override
    @Transactional
    public ProductResponse update(ProductUpdateRequest request, Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tentativa de atualizar produto inexistente com ID: {}", id);
                    return new ProductNotFoundException("Produto não encontrado");
                });

        if(product.getStatus() == ProductStatus.ARCHIVED){
            log.warn("Falha ao atualizar o produto: Produto ID {} está arquivado.", id);
            throw new BusinessRuleException("Esse produto está arquivado");
        }

        if(request.name() != null){

            String name = request.name()
                    .trim()
                    .toLowerCase()
                    .replaceAll("\\s+", " ");

            if(repository.existsByNameAndIdNot(name, id)){
                log.warn("Conflito de nome ao atualizar produto ID {}: O nome '{}' já pertence a outro item.", id, request.name());
                throw new ProductAlreadyExistsException("Esse produto já está cadastrado");
            }

            product.setName(name);

        }

        if(request.description() != null){
            product.setDescription(request.description());
        }

        if(request.price() != null){
            product.setPrice(request.price());
        }

        if(request.stock() != null) {
            if(request.stock() <= 0) {
                product.setStatus(ProductStatus.INACTIVE);
            } else {
                product.setStatus(ProductStatus.ACTIVE);
            }
            product.setStock(request.stock());
        }

        log.info("Produto ID {} atualizado com sucesso.", id);
        return ProductMapper.toProductResponse(product);

    }

    @Override
    @Transactional
    public void archive(Long id) {

        log.info("Iniciando arquivamento do produto ID: {}", id);

        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tentativa de arquivar produto inexistente com ID: {}", id);
                    return new ProductNotFoundException("Produto não encontrado");
                });

        if (product.getStatus() == ProductStatus.ARCHIVED) {
            log.warn("Tentativa de arquivar produto já arquivado com ID: {}", id);
            throw new BusinessRuleException("Esse produto já está arquivado");
        }

        product.setStatus(ProductStatus.ARCHIVED);

        log.info("Produto ID {} arquivado com sucesso.", id);

    }

    @Override
    @Transactional
    public void desarchive(Long id) {

        log.info("Iniciando desarquivamento do produto ID: {}", id);

        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tentativa de desarquivar produto inexistente com ID: {}", id);
                    return new ProductNotFoundException("Produto não encontrado");
                });

        if (product.getStatus() == ProductStatus.ACTIVE || product.getStatus() == ProductStatus.INACTIVE) {
            log.warn("Tentativa de desarquivar produto já desarquivado com ID: {}", id);
            throw new BusinessRuleException("Esse produto já está desarquivado");
        }

        if(product.getStock() == 0){
            product.setStatus(ProductStatus.INACTIVE);
        } else {
            product.setStatus(ProductStatus.ACTIVE);
        }

        log.info("Produto ID {} desarquivado com sucesso.", id);

    }

    @Override
    public Integer getStock(Long id) {
        log.debug("Consulta síncrona de estoque solicitada para o produto ID: {}", id);
        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Falha na consulta de estoque: Produto ID {} não encontrado.", id);
                    return new ProductNotFoundException("Produto não encontrado");
                });
        return product.getStock();
    }

    @Override
    public BigDecimal getProductPrice(Long id) {
        log.debug("Consulta síncrona de preço solicitada para o produto ID: {}", id);
        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Falha na consulta de preço: Produto ID {} não encontrado.", id);
                    return new ProductNotFoundException("Produto não encontrado");
                });
        return product.getPrice();
    }

    @Override
    @Transactional
    public void decreaseStock(Long id, Integer quantity) {

        log.info("Atualizando estoque para o produto ID: {} decrementando quantidade: {}", id, quantity);

        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Falha ao encontrar o produto para atualizar estoque: Produto ID {} não encontrado.", id);
                    return new ProductNotFoundException("Produto não encontrado");
                });

        if(product.getStatus() == ProductStatus.ARCHIVED){
            log.warn("Falha ao atualizar o estoque: Produto ID {} está arquivado.", id);
            throw new BusinessRuleException("Esse produto está arquivado");
        }

        int newStock = product.getStock() - quantity;
        product.setStock(Math.max(0, newStock));

        if(product.getStock() == 0) {
            product.setStatus(ProductStatus.INACTIVE);
            log.warn("Estoque do produto ID {} chegou a zero. Status alterado para INACTIVE.", product.getId());
        }

        log.info("Estoque atualizado com sucesso. Novo estoque: {}", product.getStock());
    }

    @Override
    public void checkArchived(Long id) {

        log.info("Iniciando a verificação do status do produto ID: {} ", id);

        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Falha ao encontrar o produto para verificação do status: Produto ID {} não encontrado.", id);
                    return new ProductNotFoundException("Produto não encontrado");
                });

        if(product.getStatus() == ProductStatus.ARCHIVED){
            log.info("Verificação concluída: Produto ID {} está arquivado.", id);
            throw new BusinessRuleException("Esse produto está arquivado");
        }

    }

}
