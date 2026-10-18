package dev.java.load_balanced_app.application.features.product;

import dev.java.load_balanced_app.application.interfaces.ProductRepositoryPort;
import dev.java.load_balanced_app.domain.entity.ProductModel;
import dev.java.load_balanced_app.domain.exception.ProductNotFoundException;
import dev.java.load_balanced_app.domain.valueobject.ProductId;

public class FindProductByIdUseCaseImpl implements FindProductByIdUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public FindProductByIdUseCaseImpl(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public ProductModel execute(Long id) {
        return productRepositoryPort.findById(new ProductId(id))
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));
    }

}
