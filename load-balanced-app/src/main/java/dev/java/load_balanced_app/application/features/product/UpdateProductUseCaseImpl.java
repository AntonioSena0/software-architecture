package dev.java.load_balanced_app.application.features.product;

import dev.java.load_balanced_app.application.command.UpdateProductCommand;
import dev.java.load_balanced_app.application.interfaces.ProductRepositoryPort;
import dev.java.load_balanced_app.domain.entity.ProductModel;
import dev.java.load_balanced_app.domain.enums.ProductStatus;
import dev.java.load_balanced_app.domain.exception.BusinessRuleException;
import dev.java.load_balanced_app.domain.exception.ProductAlreadyExistsException;
import dev.java.load_balanced_app.domain.exception.ProductNotFoundException;
import dev.java.load_balanced_app.domain.valueobject.Name;
import dev.java.load_balanced_app.domain.valueobject.Price;
import dev.java.load_balanced_app.domain.valueobject.ProductId;
import dev.java.load_balanced_app.domain.valueobject.Stock;

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
