package dev.java.load_balanced_app.application.interfaces;

import dev.java.load_balanced_app.domain.entity.ProductModel;
import dev.java.load_balanced_app.domain.valueobject.ProductId;
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
