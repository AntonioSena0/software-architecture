package dev.java.clean_architecture.application.features.product;

import dev.java.clean_architecture.application.interfaces.ProductRepositoryPort;
import dev.java.clean_architecture.domain.entity.ProductModel;
import dev.java.clean_architecture.domain.exception.ProductNotFoundException;
import dev.java.clean_architecture.domain.valueobject.ProductId;

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
