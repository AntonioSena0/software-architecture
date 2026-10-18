package dev.java.load_balanced_app.application.features.product;

import dev.java.load_balanced_app.domain.entity.ProductModel;

public interface CreateProductUseCase {

    ProductModel execute(ProductModel product);

}
