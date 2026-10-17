package dev.java.hexagonal_architecture.product.ports.inbound.features;

import dev.java.hexagonal_architecture.product.domain.enums.ProductStatus;
import dev.java.hexagonal_architecture.product.domain.exception.BusinessRuleException;
import dev.java.hexagonal_architecture.product.domain.exception.ProductNotFoundException;
import dev.java.hexagonal_architecture.product.domain.model.ProductModel;
import dev.java.hexagonal_architecture.product.domain.valueobject.ProductId;
import dev.java.hexagonal_architecture.product.ports.outbound.ProductRepositoryPort;

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
