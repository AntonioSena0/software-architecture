package dev.java.hexagonal_architecture.product.adapters.outbound.persistence.repository;

import dev.java.hexagonal_architecture.product.adapters.outbound.persistence.mapper.ProductPersistenceMapper;
import dev.java.hexagonal_architecture.product.domain.model.ProductModel;
import dev.java.hexagonal_architecture.product.domain.valueobject.ProductId;
import dev.java.hexagonal_architecture.product.ports.outbound.ProductRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final SpringDataProductRepository productRepository;

    @Override
    public Page<ProductModel> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductPersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductModel> findById(ProductId id) {
        return productRepository.findById(id.value())
                .map(ProductPersistenceMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndIdNot(String name, ProductId id) {
        return productRepository.existsByNameAndIdNot(name, id.value());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    @Transactional
    public ProductModel save(ProductModel productModel) {
        return ProductPersistenceMapper.toDomain(productRepository.save(ProductPersistenceMapper.toEntity(productModel)));
    }

}