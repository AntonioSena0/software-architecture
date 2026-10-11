package dev.java.clean_architecture.application.features.product;

import dev.java.clean_architecture.application.interfaces.ProductRepositoryPort;
import dev.java.clean_architecture.domain.entity.ProductModel;
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
