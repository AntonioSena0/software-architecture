package dev.java.hexagonal_architecture.product.ports.inbound.features;

import dev.java.hexagonal_architecture.product.domain.model.ProductModel;

public interface FindProductByIdUseCase {

    ProductModel execute(Long id);

}
