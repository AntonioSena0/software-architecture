package dev.java.hexagonal_architecture.product.ports.outbound;

import dev.java.hexagonal_architecture.product.domain.model.ProductModel;
import dev.java.hexagonal_architecture.product.domain.valueobject.ProductId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepositoryPort {

    Page<ProductModel> findAll(Pageable pageable);
    Optional<ProductModel> findById(ProductId id);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, ProductId id);
    ProductModel save(ProductModel productModel);

}
