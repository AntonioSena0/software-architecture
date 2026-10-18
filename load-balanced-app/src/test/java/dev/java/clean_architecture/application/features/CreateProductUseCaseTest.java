package dev.java.clean_architecture.application.features;

import dev.java.load_balanced_app.api.dto.request.ProductRequest;
import dev.java.load_balanced_app.api.mapper.ProductRestMapper;
import dev.java.load_balanced_app.application.features.product.CreateProductUseCaseImpl;
import dev.java.load_balanced_app.application.interfaces.ProductRepositoryPort;
import dev.java.load_balanced_app.domain.entity.ProductModel;
import dev.java.load_balanced_app.domain.exception.ProductAlreadyExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class CreateProductUseCaseTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private CreateProductUseCaseImpl createProductUseCase;

    @Test
    @DisplayName("Deve criar um produto com sucesso quando os dados forem válidados e o nome for único")
    void shouldCreateProductSuccessfully() {

        ProductRequest request = new ProductRequest(
                "Smartphone",
                "Smartphone 128GB",
                new BigDecimal("1500.00"),
                15
        );

        Mockito.when(productRepositoryPort.existsByName(Mockito.anyString())).thenReturn(false);
        Mockito.when(productRepositoryPort.save(Mockito.any(ProductModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductModel result = createProductUseCase.execute(ProductRestMapper.toDomain(request));

        Assertions.assertNotNull(result);
        Assertions.assertEquals("smartphone", result.getName().value());
        Assertions.assertEquals(new BigDecimal("1500.00"), result.getPrice().value());
        Assertions.assertEquals(15, result.getStock().value());

        Mockito.verify(productRepositoryPort, Mockito.times(1)).existsByName("smartphone");
        Mockito.verify(productRepositoryPort, Mockito.times(1)).save(Mockito.any(ProductModel.class));

    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar criar um produto com um nome que já existe")
    void shouldThrowExceptionWhenProductNameAlreadyExists() {

        ProductRequest request = new ProductRequest(
                "Smartphone",
                "Smartphone 128GB",
                new BigDecimal("1500.00"),
                15
        );

        Mockito.when(productRepositoryPort.existsByName("smartphone")).thenReturn(true);

        Exception exception = Assertions.assertThrows(ProductAlreadyExistsException.class, () -> {
            createProductUseCase.execute(ProductRestMapper.toDomain(request));
        });

        Assertions.assertEquals("Já existe um produto com este nome", exception.getMessage());

        Mockito.verify(productRepositoryPort, Mockito.times(1)).existsByName("smartphone");
        Mockito.verify(productRepositoryPort, Mockito.never()).save(Mockito.any(ProductModel.class));

    }

}
