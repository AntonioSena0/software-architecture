package dev.java.load_balanced_app.application.features.product;

import dev.java.load_balanced_app.domain.entity.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindAllProductsUseCase {

    Page<ProductModel> execute(Pageable pageable);

}
