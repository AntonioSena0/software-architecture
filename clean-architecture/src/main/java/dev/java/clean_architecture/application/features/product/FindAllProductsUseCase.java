package dev.java.clean_architecture.application.features.product;

import dev.java.clean_architecture.domain.entity.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindAllProductsUseCase {

    Page<ProductModel> execute(Pageable pageable);

}
