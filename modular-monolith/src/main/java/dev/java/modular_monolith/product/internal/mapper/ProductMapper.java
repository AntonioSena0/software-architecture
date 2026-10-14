package dev.java.modular_monolith.product.internal.mapper;

import dev.java.modular_monolith.product.internal.entity.Product;
import dev.java.modular_monolith.product.ProductStatus;
import dev.java.modular_monolith.product.internal.dto.ProductRequest;
import dev.java.modular_monolith.product.internal.dto.ProductResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductMapper {

    public Product toProduct(ProductRequest request, ProductStatus status){

        return Product
                .builder()
                .name(request.name()
                        .trim()
                        .toLowerCase()
                        .replaceAll("\\s+", " ")
                )
                .description(request.description() != null ? request.description() : "Não possui descrição")
                .price(request.price())
                .stock(request.stock())
                .status(status)
                .build();

    }

    public ProductResponse toProductResponse(Product product) {

        return ProductResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();

    }

}
