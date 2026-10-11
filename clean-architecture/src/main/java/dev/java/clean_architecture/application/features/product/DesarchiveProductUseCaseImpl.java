package dev.java.clean_architecture.application.features.product;

import dev.java.clean_architecture.application.interfaces.ProductRepositoryPort;
import dev.java.clean_architecture.domain.entity.ProductModel;
import dev.java.clean_architecture.domain.enums.ProductStatus;
import dev.java.clean_architecture.domain.exception.BusinessRuleException;
import dev.java.clean_architecture.domain.exception.ProductNotFoundException;
import dev.java.clean_architecture.domain.valueobject.ProductId;

public class DesarchiveProductUseCaseImpl implements DesarchiveProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public DesarchiveProductUseCaseImpl(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public void execute(Long id) {

        ProductId productId = new ProductId(id);

        ProductModel product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));

        if(product.getStatus() == ProductStatus.ACTIVE || product.getStatus() == ProductStatus.INACTIVE) {
            throw new BusinessRuleException("O produto já está desarquivado");
        }

        product.desarchive(product.getStock());

        productRepositoryPort.save(product);

    }

}
