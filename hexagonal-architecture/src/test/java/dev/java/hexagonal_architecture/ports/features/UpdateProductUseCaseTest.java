package dev.java.hexagonal_architecture.ports.features;

import dev.java.hexagonal_architecture.product.domain.exception.BusinessRuleException;
import dev.java.hexagonal_architecture.product.domain.exception.ProductAlreadyExistsException;
import dev.java.hexagonal_architecture.product.domain.exception.ProductNotFoundException;
import dev.java.hexagonal_architecture.product.domain.model.ProductModel;
import dev.java.hexagonal_architecture.product.domain.valueobject.ProductId;
import dev.java.hexagonal_architecture.product.ports.inbound.command.UpdateProductCommand;
import dev.java.hexagonal_architecture.product.ports.inbound.features.UpdateProductUseCaseImpl;
import dev.java.hexagonal_architecture.product.ports.outbound.ProductRepositoryPort;
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
public class UpdateProductUseCaseTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private UpdateProductUseCaseImpl updateProductUseCase;

    @Test
    @DisplayName("Deve atualizar apenas os campos enviados no comando e manter os antigos (Merge)")
    void shouldUpdateProductSuccessfullyWithPartialData() {

        Long productId = 1L;

        ProductModel existingProduct = ProductModel.createNew(
                "Notebook antigo",
                "Descrição antiga",
                new BigDecimal("3000.00"),
                10
        );

        UpdateProductCommand command = new UpdateProductCommand(
                null,
                null,
                new BigDecimal("3500.00"),
                null
        );

        Mockito.when(productRepositoryPort.findById(Mockito.any())).thenReturn(Optional.of(existingProduct));
        Mockito.when(productRepositoryPort.save(Mockito.any(ProductModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductModel result = updateProductUseCase.execute(command, productId);

        Assertions.assertNotNull(result);

        Assertions.assertEquals("notebook antigo", result.getName().value());
        Assertions.assertEquals("Descrição antiga", result.getDescription());
        Assertions.assertEquals(new BigDecimal("3500.00"), result.getPrice().value());
        Assertions.assertEquals(10, result.getStock().value());

        Mockito.verify(productRepositoryPort, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(productRepositoryPort, Mockito.times(1)).save(Mockito.any(ProductModel.class));

    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar um produto que não existe")
    void shouldThrowExceptionWhenProductNotFound() {

        Long productId = 99L;

        UpdateProductCommand command = new UpdateProductCommand(
                "Novo Nome",
                "Nova descrição",
                new BigDecimal("100.00"),
                5
        );

        Mockito.when(productRepositoryPort.findById(Mockito.any())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(ProductNotFoundException.class, () -> {
            updateProductUseCase.execute(command, productId);
        });

        Assertions.assertEquals("Produto não encontrado", exception.getMessage());

        Mockito.verify(productRepositoryPort, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(productRepositoryPort, Mockito.never()).save(Mockito.any());

    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar com um nome que já existe em outro produto")
    void shouldThrowExceptionWhenTheProductAlreadyExists() {

        Long productId = 1L;

        ProductModel existingProduct = ProductModel.createNew(
                "Notebook Antigo",
                "Descrição antiga",
                new BigDecimal("3000.00"),
                10
        );

        UpdateProductCommand command = new UpdateProductCommand(
                "Notebook Novo",
                null,
                new BigDecimal("10000.00"),
                null
        );

        Mockito.when(productRepositoryPort.findById(Mockito.any())).thenReturn(Optional.of(existingProduct));
        Mockito.when(productRepositoryPort.existsByNameAndIdNot(Mockito.eq("notebook novo"), Mockito.eq(new ProductId(productId)))).thenReturn(true);

        Exception exception = Assertions.assertThrows(ProductAlreadyExistsException.class, () -> {
            updateProductUseCase.execute(command, productId);
        });

        Assertions.assertEquals("Já existe outro produto com este nome", exception.getMessage());

        Mockito.verify(productRepositoryPort, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(productRepositoryPort, Mockito.never()).save(Mockito.any());

    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar com um produto arquivado")
    void shouldThrowExceptionWhenProductIsArchived() {

        Long productId = 1L;

        ProductModel existingProduct = ProductModel.createNew(
                "Notebook Antigo",
                "Descrição antiga",
                new BigDecimal("3000.00"),
                10
        );

        existingProduct.archive();

        UpdateProductCommand command = new UpdateProductCommand(
                "Notebook Novo",
                null,
                new BigDecimal("10000.00"),
                null
        );

        Mockito.when(productRepositoryPort.findById(Mockito.any())).thenReturn(Optional.of(existingProduct));

        Exception exception = Assertions.assertThrows(BusinessRuleException.class, () -> {
            updateProductUseCase.execute(command, productId);
        });

        Assertions.assertEquals("Esse produto está arquivado", exception.getMessage());

        Mockito.verify(productRepositoryPort, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(productRepositoryPort, Mockito.never()).save(Mockito.any());

    }

}
