package dev.java.clean_architecture.application.features;

import dev.java.load_balanced_app.application.features.product.DesarchiveProductUseCaseImpl;
import dev.java.load_balanced_app.application.interfaces.ProductRepositoryPort;
import dev.java.load_balanced_app.domain.entity.ProductModel;
import dev.java.load_balanced_app.domain.enums.ProductStatus;
import dev.java.load_balanced_app.domain.exception.BusinessRuleException;
import dev.java.load_balanced_app.domain.exception.ProductNotFoundException;
import dev.java.load_balanced_app.domain.valueobject.ProductId;
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
public class DesarchiveProductUseCaseTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private DesarchiveProductUseCaseImpl desarchiveProductUseCase;

    @Test
    @DisplayName("Deve desarquivar um produto com sucesso")
    void shouldDesarchiveProductSuccessfully() {

        Long productId = 1L;

        ProductModel existingProduct = ProductModel.createNew(
                "Monitor",
                "Monitor 24 polegadas",
                new BigDecimal("900.00"),
                5
        );

        existingProduct.archive();

        Mockito.when(productRepositoryPort.findById(Mockito.any(ProductId.class))).thenReturn(Optional.of(existingProduct));
        Mockito.when(productRepositoryPort.save(Mockito.any(ProductModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        desarchiveProductUseCase.execute(productId);

        Assertions.assertEquals(ProductStatus.ACTIVE, existingProduct.getStatus());

        Mockito.verify(productRepositoryPort, Mockito.times(1)).findById(Mockito.any(ProductId.class));
        Mockito.verify(productRepositoryPort, Mockito.times(1)).save(Mockito.any(ProductModel.class));

    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar desarquivar um produto que não existe")
    void shouldThrowExceptionWhenProductNotFound() {

        Long productId = 99L;

        Mockito.when(productRepositoryPort.findById(Mockito.any())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(ProductNotFoundException.class, () -> {
            desarchiveProductUseCase.execute(productId);
        });

        Assertions.assertEquals("Produto não encontrado", exception.getMessage());

        Mockito.verify(productRepositoryPort, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(productRepositoryPort, Mockito.never()).save(Mockito.any());

    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar arquivar um produto que já está desarquivado")
    void shouldThrowExceptionWhenProductIsAlreadyDesarchived() {

        Long productId = 1L;

        ProductModel existingProduct = ProductModel.createNew(
                "Notebook Antigo",
                "Descrição antiga",
                new BigDecimal("3000.00"),
                10
        );

        Mockito.when(productRepositoryPort.findById(Mockito.any())).thenReturn(Optional.of(existingProduct));

        Exception exception = Assertions.assertThrows(BusinessRuleException.class, () -> {
            desarchiveProductUseCase.execute(productId);
        });

        Assertions.assertEquals("O produto já está desarquivado", exception.getMessage());

        Mockito.verify(productRepositoryPort, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(productRepositoryPort, Mockito.never()).save(Mockito.any());

    }

}
