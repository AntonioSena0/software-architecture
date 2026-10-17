package dev.java.hexagonal_architecture.product.ports.inbound.features;

import dev.java.hexagonal_architecture.product.domain.exception.ProductNotFoundException;
import dev.java.hexagonal_architecture.product.domain.model.ProductModel;
import dev.java.hexagonal_architecture.product.domain.valueobject.ProductId;
import dev.java.hexagonal_architecture.product.ports.outbound.ProductRepositoryPort;

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
