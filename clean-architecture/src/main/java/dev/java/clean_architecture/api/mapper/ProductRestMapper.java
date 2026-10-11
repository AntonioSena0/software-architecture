package dev.java.clean_architecture.api.mapper;

import dev.java.clean_architecture.api.dto.request.ProductRequest;
import dev.java.clean_architecture.api.dto.response.ProductResponse;
import dev.java.clean_architecture.domain.entity.ProductModel;
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
