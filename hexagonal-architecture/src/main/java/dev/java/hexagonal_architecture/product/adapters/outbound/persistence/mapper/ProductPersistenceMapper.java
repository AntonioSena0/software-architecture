package dev.java.hexagonal_architecture.product.adapters.outbound.persistence.mapper;

import dev.java.hexagonal_architecture.product.adapters.outbound.persistence.entity.ProductJpaEntity;
import dev.java.hexagonal_architecture.product.domain.model.ProductModel;
import dev.java.hexagonal_architecture.product.domain.valueobject.Name;
import dev.java.hexagonal_architecture.product.domain.valueobject.Price;
import dev.java.hexagonal_architecture.product.domain.valueobject.ProductId;
import dev.java.hexagonal_architecture.product.domain.valueobject.Stock;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductPersistenceMapper {

    public ProductJpaEntity toEntity(ProductModel product) {

        return ProductJpaEntity
                .builder()
                .id((product.getId() != null) ? product.getId().value() : null)
                .name(product.getName().value())
                .description(product.getDescription())
                .price(product.getPrice().value())
                .stock(product.getStock().value())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();

    }

    public ProductModel toDomain(ProductJpaEntity entity) {

        return new ProductModel(
                new ProductId(entity.getId()),
                new Name(entity.getName()),
                entity.getDescription(),
                new Price(entity.getPrice()),
                new Stock(entity.getStock()),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );

    }

}
