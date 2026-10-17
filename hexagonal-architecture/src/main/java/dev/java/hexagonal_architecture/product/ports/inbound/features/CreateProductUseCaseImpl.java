package dev.java.hexagonal_architecture.product.ports.inbound.features;

import dev.java.hexagonal_architecture.product.domain.exception.ProductAlreadyExistsException;
import dev.java.hexagonal_architecture.product.domain.model.ProductModel;
import dev.java.hexagonal_architecture.product.ports.outbound.ProductRepositoryPort;

public class CreateProductUseCaseImpl implements CreateProductUseCase{

    private final ProductRepositoryPort productRepositoryPort;

    public CreateProductUseCaseImpl(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public ProductModel execute(ProductModel productModel) {

        if(productRepositoryPort.existsByName(productModel.getName().value())){
            throw new ProductAlreadyExistsException("Já existe um produto com este nome");
        }

        return productRepositoryPort.save(productModel);
    }

}
