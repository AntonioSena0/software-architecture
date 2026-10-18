package dev.java.load_balanced_app.application.features.product;

import dev.java.load_balanced_app.application.interfaces.ProductRepositoryPort;
import dev.java.load_balanced_app.domain.entity.ProductModel;
import dev.java.load_balanced_app.domain.enums.ProductStatus;
import dev.java.load_balanced_app.domain.exception.BusinessRuleException;
import dev.java.load_balanced_app.domain.exception.ProductNotFoundException;
import dev.java.load_balanced_app.domain.valueobject.ProductId;

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
