package dev.java.clean_architecture.application.features.product;

import dev.java.clean_architecture.domain.entity.ProductModel;

public interface FindProductByIdUseCase {

    ProductModel execute(Long id);

}
