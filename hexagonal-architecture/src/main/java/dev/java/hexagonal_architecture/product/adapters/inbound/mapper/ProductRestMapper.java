package dev.java.hexagonal_architecture.product.adapters.inbound.mapper;

import dev.java.hexagonal_architecture.product.adapters.inbound.dto.request.ProductRequest;
import dev.java.hexagonal_architecture.product.adapters.inbound.dto.response.ProductResponse;
import dev.java.hexagonal_architecture.product.domain.model.ProductModel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductRestMapper {

    public ProductModel toDomain(ProductRequest request){

        return ProductModel.createNew(
                request.name(),
                request.description(),
                request.price(),
                request.stock()
        );

    }

    public ProductResponse toProductResponse(ProductModel model){

        return ProductResponse
                .builder()
                .id(model.getId().value())
                .name(model.getName().value())
                .description(model.getDescription())
                .price(model.getPrice().value())
                .stock(model.getStock().value())
                .status(model.getStatus())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();

    }

}
