package dev.java.load_balanced_app.infrastructure.persistence.decorator;

import dev.java.load_balanced_app.application.interfaces.ProductRepositoryPort;
import dev.java.load_balanced_app.domain.entity.ProductModel;
import dev.java.load_balanced_app.domain.valueobject.ProductId;
import dev.java.load_balanced_app.infrastructure.persistence.adapter.ProductRepositoryAdapter;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
@AllArgsConstructor
public class ProductRepositoryCacheDecorator implements ProductRepositoryPort {

    private final ProductRepositoryAdapter adapter;

    @Override
    public Page<ProductModel> findAll(Pageable pageable) {
        return adapter.findAll(pageable);
    }

    @Override
    @Cacheable(value = "products", key = "#id", unless = "#result == null")
    public Optional<ProductModel> findById(ProductId id) {
        return adapter.findById(id);
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public boolean existsByName(String name) {
        return adapter.existsByName(name);
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public boolean existsByNameAndIdNot(String name, ProductId id) {
        return adapter.existsByNameAndIdNot(name, id);
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public ProductModel save(ProductModel productModel) {
        return adapter.save(productModel);
    }

}
