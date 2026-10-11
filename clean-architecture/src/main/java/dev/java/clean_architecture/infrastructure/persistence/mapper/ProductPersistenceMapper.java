package dev.java.clean_architecture.infrastructure.persistence.mapper;

import dev.java.clean_architecture.domain.entity.ProductModel;
import dev.java.clean_architecture.domain.valueobject.Name;
import dev.java.clean_architecture.domain.valueobject.Price;
import dev.java.clean_architecture.domain.valueobject.ProductId;
import dev.java.clean_architecture.domain.valueobject.Stock;
import dev.java.clean_architecture.infrastructure.persistence.entity.ProductJpaEntity;
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
