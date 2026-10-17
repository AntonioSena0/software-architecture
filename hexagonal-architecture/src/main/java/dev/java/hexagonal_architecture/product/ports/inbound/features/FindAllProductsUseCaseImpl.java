package dev.java.hexagonal_architecture.product.ports.inbound.features;

import dev.java.hexagonal_architecture.product.domain.model.ProductModel;
import dev.java.hexagonal_architecture.product.ports.outbound.ProductRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class FindAllProductsUseCaseImpl implements FindAllProductsUseCase{

    private final ProductRepositoryPort productRepositoryPort;

    public FindAllProductsUseCaseImpl(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public Page<ProductModel> execute(Pageable pageable) {
        return productRepositoryPort.findAll(pageable);
    }

}
