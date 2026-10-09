package dev.java.server_sent_events.mapper;

import dev.java.server_sent_events.dto.request.ProductRequest;
import dev.java.server_sent_events.dto.response.ProductResponse;
import dev.java.server_sent_events.entity.Product;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductMapper {

    public Product toProduct(ProductRequest request){

        return Product
                .builder()
                .name(request.name())
                .description(request.description() != null ? request.description() : "Não possui descrição")
                .price(request.price())
                .stock(request.stock())
                .build();

    }

    public ProductResponse toProductResponse(Product product){

        return ProductResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();

    }

}
