package dev.java.clean_architecture.application.features;

import dev.java.clean_architecture.application.features.product.FindProductByIdUseCaseImpl;
import dev.java.clean_architecture.application.interfaces.ProductRepositoryPort;
import dev.java.clean_architecture.domain.entity.ProductModel;
import dev.java.clean_architecture.domain.exception.ProductNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FindProductByIdUseCaseTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private FindProductByIdUseCaseImpl findProductByIdUseCase;

    @Test
    @DisplayName("Deve retornar o produto com sucesso quando o ID existir")
    void shouldFindProductByIdSuccessfully() {

        Long productId = 1L;

        ProductModel productModel = ProductModel.createNew(
                "Notebook",
                "Notebook gamer",
                new BigDecimal("3500.00"),
                10
        );

        Mockito.when(productRepositoryPort.findById(Mockito.any())).thenReturn(Optional.of(productModel));

        ProductModel result = findProductByIdUseCase.execute(productId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Notebook", result.getName().value());
        Assertions.assertEquals(new BigDecimal("3500.00"), result.getPrice().value());

        Mockito.verify(productRepositoryPort, Mockito.times(1)).findById(Mockito.any());

    }

    @Test
    @DisplayName("Deve lançar exceção quando o produto não for encontrado pelo ID")
    void shouldThrowExceptionWhenProductNotFound() {

        Long productId = 99L;

        Mockito.when(productRepositoryPort.findById(Mockito.any())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(ProductNotFoundException.class, () -> {
            findProductByIdUseCase.execute(productId);
        });

        Assertions.assertEquals("Produto não encontrado", exception.getMessage());

        Mockito.verify(productRepositoryPort, Mockito.times(1)).findById(Mockito.any());

    }

}
