package dev.java.clean_architecture.application.features.product;

import dev.java.clean_architecture.application.interfaces.ProductRepositoryPort;
import dev.java.clean_architecture.domain.entity.ProductModel;
import dev.java.clean_architecture.domain.enums.ProductStatus;
import dev.java.clean_architecture.domain.exception.BusinessRuleException;
import dev.java.clean_architecture.domain.exception.ProductNotFoundException;
import dev.java.clean_architecture.domain.valueobject.ProductId;

public class ArchiveProductUseCaseImpl implements ArchiveProductUseCase{

    private final ProductRepositoryPort productRepositoryPort;

    public ArchiveProductUseCaseImpl(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public void execute(Long id) {

        ProductId productId = new ProductId(id);

        ProductModel product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));

        if(product.getStatus() == ProductStatus.ARCHIVED) {
            throw new BusinessRuleException("O produto já está arquivado");
        }

        product.archive();

        productRepositoryPort.save(product);

    }

}
