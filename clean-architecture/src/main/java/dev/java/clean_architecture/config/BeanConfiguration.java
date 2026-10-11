package dev.java.clean_architecture.config;

import dev.java.clean_architecture.application.features.product.*;
import dev.java.clean_architecture.application.interfaces.ProductRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ArchiveProductUseCase archiveProductUseCase(ProductRepositoryPort productRepository) {
        return new ArchiveProductUseCaseImpl(productRepository);
    }

    @Bean
    public CreateProductUseCase createProductUseCase(ProductRepositoryPort productRepository) {
        return new CreateProductUseCaseImpl(productRepository);
    }

    @Bean
    public DesarchiveProductUseCase desarchiveProductUseCase(ProductRepositoryPort productRepository) {
        return new DesarchiveProductUseCaseImpl(productRepository);
    }

    @Bean
    public FindAllProductsUseCase findAllProductsUseCase(ProductRepositoryPort productRepository) {
        return new FindAllProductsUseCaseImpl(productRepository);
    }

    @Bean
    public FindProductByIdUseCase findProductByIdUseCase(ProductRepositoryPort productRepository) {
        return new FindProductByIdUseCaseImpl(productRepository);
    }

    @Bean
    public UpdateProductUseCase updateProductUseCase(ProductRepositoryPort productRepository) {
        return new UpdateProductUseCaseImpl(productRepository);
    }

}
