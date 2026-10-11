package dev.java.clean_architecture.infrastructure.persistence.adapter;

import dev.java.clean_architecture.application.interfaces.ProductRepositoryPort;
import dev.java.clean_architecture.domain.entity.ProductModel;
import dev.java.clean_architecture.domain.valueobject.ProductId;
import dev.java.clean_architecture.infrastructure.persistence.mapper.ProductPersistenceMapper;
import dev.java.clean_architecture.infrastructure.persistence.repository.SpringDataProductRepository;
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