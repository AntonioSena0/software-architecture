package dev.java.load_balanced_app.application.features.product;

import dev.java.load_balanced_app.application.interfaces.ProductRepositoryPort;
import dev.java.load_balanced_app.domain.entity.ProductModel;
import dev.java.load_balanced_app.domain.exception.ProductAlreadyExistsException;

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
