package dev.java.clean_architecture.application.features.product;

import dev.java.clean_architecture.application.command.UpdateProductCommand;
import dev.java.clean_architecture.domain.entity.ProductModel;

public interface UpdateProductUseCase {

    ProductModel execute(UpdateProductCommand productCommand, Long id);

}
