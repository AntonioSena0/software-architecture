package dev.java.hexagonal_architecture.product.ports.inbound.features;

import dev.java.hexagonal_architecture.product.domain.model.ProductModel;
import dev.java.hexagonal_architecture.product.ports.inbound.command.UpdateProductCommand;

public interface UpdateProductUseCase {

    ProductModel execute(UpdateProductCommand productCommand, Long id);

}
