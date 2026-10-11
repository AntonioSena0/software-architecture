package dev.java.clean_architecture.application.features;

import dev.java.clean_architecture.application.features.product.FindAllProductsUseCaseImpl;
import dev.java.clean_architecture.application.interfaces.ProductRepositoryPort;
import dev.java.clean_architecture.domain.entity.ProductModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FindAllUseCaseTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private FindAllProductsUseCaseImpl findAllProductsUseCase;

    @Test
    @DisplayName("Deve retornar uma página de produtos com sucesso")
    void shouldReturnPagedProductsSuccessfully() {

        Pageable pageable = PageRequest.of(0, 10);

        ProductModel productModel = ProductModel.createNew(
                "Teclado",
                "Teclado mecânico",
                new BigDecimal("250.00"),
                20
        );

        Page<ProductModel> productPage = new PageImpl<>(List.of(productModel), pageable, 1);

        Mockito.when(productRepositoryPort.findAll(Mockito.any(Pageable.class))).thenReturn(productPage);

        Page<ProductModel> result = findAllProductsUseCase.execute(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals("Teclado", result.getContent().getFirst().getName().value());

        Mockito.verify(productRepositoryPort, Mockito.times(1)).findAll(Mockito.any(Pageable.class));

    }

}
