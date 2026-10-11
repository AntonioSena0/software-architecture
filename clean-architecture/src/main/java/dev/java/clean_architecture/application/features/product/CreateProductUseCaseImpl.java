package dev.java.clean_architecture.application.features.product;

import dev.java.clean_architecture.application.interfaces.ProductRepositoryPort;
import dev.java.clean_architecture.domain.entity.ProductModel;
import dev.java.clean_architecture.domain.exception.ProductAlreadyExistsException;

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
