package dev.java.modular_monolith.product.service;

import dev.java.modular_monolith.product.ProductCreatedEvent;
import dev.java.modular_monolith.product.ProductStatus;
import dev.java.modular_monolith.product.internal.dto.ProductRequest;
import dev.java.modular_monolith.product.internal.dto.ProductResponse;
import dev.java.modular_monolith.product.internal.dto.ProductUpdateRequest;
import dev.java.modular_monolith.product.internal.entity.Product;
import dev.java.modular_monolith.product.internal.exception.BusinessRuleException;
import dev.java.modular_monolith.product.internal.exception.ProductAlreadyExistsException;
import dev.java.modular_monolith.product.internal.exception.ProductNotFoundException;
import dev.java.modular_monolith.product.internal.repository.ProductRepository;
import dev.java.modular_monolith.product.internal.service.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("Deve retornar lista paginada de produtos quando existir produtos cadastrados")
    void shouldReturnPaginatedProductsWhenProductsExist() {
        Pageable pageable = PageRequest.of(0, 10);

        Product product = Product
                .builder()
                .id(1L)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ACTIVE)
                .build();

        Page<Product> productPage = new PageImpl<>(List.of(product), pageable, 1);

        Mockito.when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<ProductResponse> result = productService.findAll(pageable);

        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals("smartphone", result.getContent().get(0).name());
        Assertions.assertEquals(ProductStatus.ACTIVE, result.getContent().get(0).status());
    }

    @Test
    @DisplayName("Deve retornar lista paga vazia quando nao existir produtos cadastrados")
    void shouldReturnEmptyPageWhenNoProductsExist() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        Mockito.when(productRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<ProductResponse> result = productService.findAll(pageable);

        Assertions.assertTrue(result.getContent().isEmpty());
        Assertions.assertEquals(0, result.getTotalElements());
    }

    @Test
    @DisplayName("Deve retornar produto por ID quando produto existir")
    void shouldReturnProductByIdWhenProductExists() {
        Long productId = 1L;

        Product product = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ACTIVE)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductResponse result = productService.findById(productId);

        Assertions.assertEquals(productId, result.id());
        Assertions.assertEquals("smartphone", result.name());
        Assertions.assertEquals(new BigDecimal("999.99"), result.price());
        Assertions.assertEquals(10, result.stock());
        Assertions.assertEquals(ProductStatus.ACTIVE, result.status());
    }

    @Test
    @DisplayName("Deve lancar ProductNotFoundException quando produto nao existir")
    void shouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        Long productId = 999L;

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.findById(productId));
    }

    @Test
    @DisplayName("Deve criar produto com status ACTIVE quando estoque for maior que zero")
    void shouldCreateProductWithActiveStatusWhenStockIsPositive() {
        ProductRequest request = new ProductRequest("Smartphone", "Smartphone descricao", new BigDecimal("999.99"), 10);

        Product savedProduct = Product
                .builder()
                .id(1L)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ACTIVE)
                .build();

        Mockito.when(productRepository.existsByName("smartphone")).thenReturn(false);
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(savedProduct);

        ProductResponse result = productService.create(request);

        Assertions.assertEquals(1L, result.id());
        Assertions.assertEquals("smartphone", result.name());
        Assertions.assertEquals(ProductStatus.ACTIVE, result.status());
        Mockito.verify(eventPublisher, Mockito.times(1)).publishEvent(Mockito.any(ProductCreatedEvent.class));
    }

    @Test
    @DisplayName("Deve criar produto com status INACTIVE quando estoque for igual a zero")
    void shouldCreateProductWithInactiveStatusWhenStockIsZero() {
        ProductRequest request = new ProductRequest("Smartphone", "Smartphone descricao", new BigDecimal("999.99"), 0);

        Product savedProduct = Product
                .builder()
                .id(1L)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(0)
                .status(ProductStatus.INACTIVE)
                .build();

        Mockito.when(productRepository.existsByName("smartphone")).thenReturn(false);
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(savedProduct);

        ProductResponse result = productService.create(request);

        Assertions.assertEquals(ProductStatus.INACTIVE, result.status());
    }

    @Test
    @DisplayName("Deve lancar ProductAlreadyExistsException quando nome do produto ja existir")
    void shouldThrowProductAlreadyExistsExceptionWhenProductNameAlreadyExists() {
        ProductRequest request = new ProductRequest("Smartphone", "Smartphone descricao", new BigDecimal("999.99"), 10);

        Mockito.when(productRepository.existsByName("smartphone")).thenReturn(true);

        Assertions.assertThrows(ProductAlreadyExistsException.class, () -> productService.create(request));
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any(Product.class));
        Mockito.verify(eventPublisher, Mockito.never()).publishEvent(Mockito.any(ProductCreatedEvent.class));
    }

    @Test
    @DisplayName("Deve publicar ProductCreatedEvent apos criar produto com sucesso")
    void shouldPublishProductCreatedEventAfterCreatingProduct() {
        ProductRequest request = new ProductRequest("Smartphone", "Smartphone descricao", new BigDecimal("999.99"), 10);

        Product savedProduct = Product
                .builder()
                .id(1L)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ACTIVE)
                .build();

        Mockito.when(productRepository.existsByName("smartphone")).thenReturn(false);
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(savedProduct);

        productService.create(request);

        ArgumentCaptor<ProductCreatedEvent> eventCaptor = ArgumentCaptor.forClass(ProductCreatedEvent.class);
        Mockito.verify(eventPublisher).publishEvent(eventCaptor.capture());

        ProductCreatedEvent event = eventCaptor.getValue();
        Assertions.assertEquals(1L, event.id());
        Assertions.assertEquals("smartphone", event.name());
        Assertions.assertEquals(new BigDecimal("999.99"), event.price());
        Assertions.assertEquals(10, event.stock());
        Assertions.assertEquals(ProductStatus.ACTIVE, event.status());
    }

    @Test
    @DisplayName("Deve atualizar todos os campos do produto quando todos forem informados")
    void shouldUpdateAllProductFieldsWhenAllAreProvided() {
        Long productId = 1L;

        Product existingProduct = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ACTIVE)
                .build();

        ProductUpdateRequest request = new ProductUpdateRequest("Smartphone Pro", "Nova descricao", new BigDecimal("1299.99"), 20);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        Mockito.when(productRepository.existsByNameAndIdNot("smartphone pro", productId)).thenReturn(false);

        ProductResponse result = productService.update(request, productId);

        Assertions.assertEquals("smartphone pro", result.name());
        Assertions.assertEquals("Nova descricao", result.description());
        Assertions.assertEquals(new BigDecimal("1299.99"), result.price());
        Assertions.assertEquals(20, result.stock());
        Assertions.assertEquals(ProductStatus.ACTIVE, result.status());
    }

    @Test
    @DisplayName("Deve atualizar apenas nome do produto quando apenas nome for informado")
    void shouldUpdateOnlyProductNameWhenOnlyNameIsProvided() {
        Long productId = 1L;

        Product existingProduct = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .description("Descricao original")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ACTIVE)
                .build();

        ProductUpdateRequest request = new ProductUpdateRequest("Smartphone Pro", null, null, null);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        Mockito.when(productRepository.existsByNameAndIdNot("smartphone pro", productId)).thenReturn(false);

        ProductResponse result = productService.update(request, productId);

        Assertions.assertEquals("smartphone pro", result.name());
        Assertions.assertEquals("Descricao original", result.description());
        Assertions.assertEquals(new BigDecimal("999.99"), result.price());
        Assertions.assertEquals(10, result.stock());
    }

    @Test
    @DisplayName("Deve alterar status para INACTIVE quando estoque atualizado for igual a zero")
    void shouldChangeStatusToInactiveWhenUpdatedStockIsZero() {
        Long productId = 1L;

        Product existingProduct = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ACTIVE)
                .build();

        ProductUpdateRequest request = new ProductUpdateRequest(null, null, null, 0);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        ProductResponse result = productService.update(request, productId);

        Assertions.assertEquals(ProductStatus.INACTIVE, result.status());
        Assertions.assertEquals(0, result.stock());
    }

    @Test
    @DisplayName("Deve alterar status para ACTIVE quando estoque atualizado for maior que zero")
    void shouldChangeStatusToActiveWhenUpdatedStockIsPositive() {
        Long productId = 1L;

        Product existingProduct = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(0)
                .status(ProductStatus.INACTIVE)
                .build();

        ProductUpdateRequest request = new ProductUpdateRequest(null, null, null, 5);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        ProductResponse result = productService.update(request, productId);

        Assertions.assertEquals(ProductStatus.ACTIVE, result.status());
        Assertions.assertEquals(5, result.stock());
    }

    @Test
    @DisplayName("Deve lancar ProductNotFoundException quando atualizar produto inexistente")
    void shouldThrowProductNotFoundExceptionWhenUpdatingNonExistentProduct() {
        Long productId = 999L;
        ProductUpdateRequest request = new ProductUpdateRequest("Smartphone", null, null, null);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.update(request, productId));
    }

    @Test
    @DisplayName("Deve lancar BusinessRuleException quando atualizar produto arquivado")
    void shouldThrowBusinessRuleExceptionWhenUpdatingArchivedProduct() {
        Long productId = 1L;

        Product archivedProduct = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ARCHIVED)
                .build();

        ProductUpdateRequest request = new ProductUpdateRequest("Smartphone Pro", null, null, null);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(archivedProduct));

        Assertions.assertThrows(BusinessRuleException.class, () -> productService.update(request, productId));
    }

    @Test
    @DisplayName("Deve lancar ProductAlreadyExistsException quando nome ja pertencer a outro produto")
    void shouldThrowProductAlreadyExistsExceptionWhenNameBelongsToAnotherProduct() {
        Long productId = 1L;

        Product existingProduct = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ACTIVE)
                .build();

        ProductUpdateRequest request = new ProductUpdateRequest("Notebook", null, null, null);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        Mockito.when(productRepository.existsByNameAndIdNot("notebook", productId)).thenReturn(true);

        Assertions.assertThrows(ProductAlreadyExistsException.class, () -> productService.update(request, productId));
    }

    @Test
    @DisplayName("Deve arquivar produto quando produto estiver ativo")
    void shouldArchiveProductWhenProductIsActive() {
        Long productId = 1L;

        Product product = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ACTIVE)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.archive(productId);

        Assertions.assertEquals(ProductStatus.ARCHIVED, product.getStatus());
    }

    @Test
    @DisplayName("Deve lancar ProductNotFoundException quando arquivar produto inexistente")
    void shouldThrowProductNotFoundExceptionWhenArchivingNonExistentProduct() {
        Long productId = 999L;

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.archive(productId));
    }

    @Test
    @DisplayName("Deve lancar BusinessRuleException quando arquivar produto ja arquivado")
    void shouldThrowBusinessRuleExceptionWhenArchivingAlreadyArchivedProduct() {
        Long productId = 1L;

        Product archivedProduct = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ARCHIVED)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(archivedProduct));

        Assertions.assertThrows(BusinessRuleException.class, () -> productService.archive(productId));
    }

    @Test
    @DisplayName("Deve desarquivar produto para ACTIVE quando estoque for maior que zero")
    void shouldDesarchiveProductToActiveWhenStockIsPositive() {
        Long productId = 1L;

        Product product = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ARCHIVED)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.desarchive(productId);

        Assertions.assertEquals(ProductStatus.ACTIVE, product.getStatus());
    }

    @Test
    @DisplayName("Deve desarquivar produto para INACTIVE quando estoque for igual a zero")
    void shouldDesarchiveProductToInactiveWhenStockIsZero() {
        Long productId = 1L;

        Product product = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(0)
                .status(ProductStatus.ARCHIVED)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.desarchive(productId);

        Assertions.assertEquals(ProductStatus.INACTIVE, product.getStatus());
    }

    @Test
    @DisplayName("Deve lancar ProductNotFoundException quando desarquivar produto inexistente")
    void shouldThrowProductNotFoundExceptionWhenDesarchivingNonExistentProduct() {
        Long productId = 999L;

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.desarchive(productId));
    }

    @Test
    @DisplayName("Deve lancar BusinessRuleException quando desarquivar produto ja ativo")
    void shouldThrowBusinessRuleExceptionWhenDesarchivingAlreadyActiveProduct() {
        Long productId = 1L;

        Product activeProduct = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ACTIVE)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(activeProduct));

        Assertions.assertThrows(BusinessRuleException.class, () -> productService.desarchive(productId));
    }

    @Test
    @DisplayName("Deve lancar BusinessRuleException quando desarquivar produto ja inativo")
    void shouldThrowBusinessRuleExceptionWhenDesarchivingAlreadyInactiveProduct() {
        Long productId = 1L;

        Product inactiveProduct = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(0)
                .status(ProductStatus.INACTIVE)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(inactiveProduct));

        Assertions.assertThrows(BusinessRuleException.class, () -> productService.desarchive(productId));
    }

    @Test
    @DisplayName("Deve retornar estoque do produto quando produto existir")
    void shouldReturnProductStockWhenProductExists() {
        Long productId = 1L;

        Product product = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ACTIVE)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Integer stock = productService.getStock(productId);

        Assertions.assertEquals(10, stock);
    }

    @Test
    @DisplayName("Deve lancar ProductNotFoundException ao consultar estoque de produto inexistente")
    void shouldThrowProductNotFoundExceptionWhenGettingStockOfNonExistentProduct() {
        Long productId = 999L;

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getStock(productId));
    }

    @Test
    @DisplayName("Deve retornar preco do produto quando produto existir")
    void shouldReturnProductPriceWhenProductExists() {
        Long productId = 1L;

        Product product = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ACTIVE)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        BigDecimal price = productService.getProductPrice(productId);

        Assertions.assertEquals(new BigDecimal("999.99"), price);
    }

    @Test
    @DisplayName("Deve lancar ProductNotFoundException ao consultar preco de produto inexistente")
    void shouldThrowProductNotFoundExceptionWhenGettingPriceOfNonExistentProduct() {
        Long productId = 999L;

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProductPrice(productId));
    }

    @Test
    @DisplayName("Deve diminuir estoque do produto quando quantidade for valida")
    void shouldDecreaseProductStockWhenQuantityIsValid() {
        Long productId = 1L;

        Product product = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ACTIVE)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.decreaseStock(productId, 3);

        Assertions.assertEquals(7, product.getStock());
        Assertions.assertEquals(ProductStatus.ACTIVE, product.getStatus());
    }

    @Test
    @DisplayName("Deve alterar status para INACTIVE quando estoque chegar a zero")
    void shouldChangeStatusToInactiveWhenStockReachesZero() {
        Long productId = 1L;

        Product product = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(5)
                .status(ProductStatus.ACTIVE)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.decreaseStock(productId, 5);

        Assertions.assertEquals(0, product.getStock());
        Assertions.assertEquals(ProductStatus.INACTIVE, product.getStatus());
    }

    @Test
    @DisplayName("Deve lancar ProductNotFoundException ao diminuir estoque de produto inexistente")
    void shouldThrowProductNotFoundExceptionWhenDecreasingStockOfNonExistentProduct() {
        Long productId = 999L;

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.decreaseStock(productId, 5));
    }

    @Test
    @DisplayName("Deve lancar BusinessRuleException ao diminuir estoque de produto arquivado")
    void shouldThrowBusinessRuleExceptionWhenDecreasingStockOfArchivedProduct() {
        Long productId = 1L;

        Product archivedProduct = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ARCHIVED)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(archivedProduct));

        Assertions.assertThrows(BusinessRuleException.class, () -> productService.decreaseStock(productId, 5));
    }

    @Test
    @DisplayName("Deve lancar BusinessRuleException ao verificar produto arquivado")
    void shouldThrowBusinessRuleExceptionWhenCheckingArchivedProduct() {
        Long productId = 1L;

        Product archivedProduct = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ARCHIVED)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(archivedProduct));

        Assertions.assertThrows(BusinessRuleException.class, () -> productService.checkArchived(productId));
    }

    @Test
    @DisplayName("Deve lancar ProductNotFoundException ao verificar produto inexistente")
    void shouldThrowProductNotFoundExceptionWhenCheckingNonExistentProduct() {
        Long productId = 999L;

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.checkArchived(productId));
    }

    @Test
    @DisplayName("Deve passar na verificacao quando produto nao estiver arquivado")
    void shouldPassVerificationWhenProductIsNotArchived() {
        Long productId = 1L;

        Product activeProduct = Product
                .builder()
                .id(productId)
                .name("smartphone")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .status(ProductStatus.ACTIVE)
                .build();

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(activeProduct));

        Assertions.assertDoesNotThrow(() -> productService.checkArchived(productId));
    }
}
