package dev.java.clean_architecture.application.features.product;

import dev.java.clean_architecture.domain.entity.ProductModel;

public interface CreateProductUseCase {

    ProductModel execute(ProductModel product);

}
