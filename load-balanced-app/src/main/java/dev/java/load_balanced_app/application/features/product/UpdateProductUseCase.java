package dev.java.load_balanced_app.application.features.product;

import dev.java.load_balanced_app.application.command.UpdateProductCommand;
import dev.java.load_balanced_app.domain.entity.ProductModel;

public interface UpdateProductUseCase {

    ProductModel execute(UpdateProductCommand productCommand, Long id);

}
