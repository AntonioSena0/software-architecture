package dev.java.hexagonal_architecture.product.ports.inbound.features;

import dev.java.hexagonal_architecture.product.domain.enums.ProductStatus;
import dev.java.hexagonal_architecture.product.domain.exception.BusinessRuleException;
import dev.java.hexagonal_architecture.product.domain.exception.ProductAlreadyExistsException;
import dev.java.hexagonal_architecture.product.domain.exception.ProductNotFoundException;
import dev.java.hexagonal_architecture.product.domain.model.ProductModel;
import dev.java.hexagonal_architecture.product.domain.valueobject.Name;
import dev.java.hexagonal_architecture.product.domain.valueobject.Price;
import dev.java.hexagonal_architecture.product.domain.valueobject.ProductId;
import dev.java.hexagonal_architecture.product.domain.valueobject.Stock;
import dev.java.hexagonal_architecture.product.ports.inbound.command.UpdateProductCommand;
import dev.java.hexagonal_architecture.product.ports.outbound.ProductRepositoryPort;

public class UpdateProductUseCaseImpl implements UpdateProductUseCase{

    private final ProductRepositoryPort productRepositoryPort;

    public UpdateProductUseCaseImpl(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public ProductModel execute(UpdateProductCommand productCommand, Long id) {

        ProductId productId = new ProductId(id);

        ProductModel product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));

        if(product.getStatus() == ProductStatus.ARCHIVED) {
            throw new BusinessRuleException("Esse produto está arquivado");
        }

        if (productCommand.name() != null && !productCommand.name().isBlank()) {
            if (productRepositoryPort.existsByNameAndIdNot(productCommand.name().trim().toLowerCase().replaceAll("\\s+", " "), productId)) {
                throw new ProductAlreadyExistsException("Já existe outro produto com este nome");
            }
        }

        Name nameToUse = (productCommand.name() == null || productCommand.name().isBlank()) ? product.getName() : new Name(productCommand.name());
        String descriptionToUse = (productCommand.description() == null) ? product.getDescription() : productCommand.description();
        Price priceToUse = (productCommand.price() == null) ? product.getPrice() : new Price(productCommand.price());
        Stock stockToUse = (productCommand.stock() == null) ? product.getStock() : new Stock(productCommand.stock());

        product.updateDetails(nameToUse, descriptionToUse, priceToUse, stockToUse);

        return productRepositoryPort.save(product);

    }

}
