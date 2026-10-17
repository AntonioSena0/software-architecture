package dev.java.hexagonal_architecture.product.ports.inbound.features;

import dev.java.hexagonal_architecture.product.domain.model.ProductModel;

public interface CreateProductUseCase {

    ProductModel execute(ProductModel product);

}
