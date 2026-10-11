package dev.java.clean_architecture.api.controller;

import dev.java.clean_architecture.api.dto.request.ProductRequest;
import dev.java.clean_architecture.api.dto.request.ProductUpdateRequest;
import dev.java.clean_architecture.api.dto.response.ApiEnvelope;
import dev.java.clean_architecture.api.dto.response.ProductResponse;
import dev.java.clean_architecture.api.mapper.ProductRestMapper;
import dev.java.clean_architecture.api.version.ApiVersion;
import dev.java.clean_architecture.application.command.UpdateProductCommand;
import dev.java.clean_architecture.application.features.product.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/" + ApiVersion.V1 + "/products")
@AllArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final FindAllProductsUseCase findAllProductsUseCase;
    private final FindProductByIdUseCase findProductByIdUseCase;
    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final ArchiveProductUseCase archiveProductUseCase;
    private final DesarchiveProductUseCase desarchiveProductUseCase;

    @Override
    @GetMapping
    public ResponseEntity<ApiEnvelope<Page<ProductResponse>>> findAll(
            @PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(ApiEnvelope.of(
                ApiVersion.V1,
                findAllProductsUseCase.execute(pageable)
                    .map(ProductRestMapper::toProductResponse))
        );
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ApiEnvelope<ProductResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiEnvelope.of(
                ApiVersion.V1,
                ProductRestMapper.toProductResponse(findProductByIdUseCase.execute(id))
        ));
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiEnvelope<ProductResponse>> create(@RequestBody @Valid ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiEnvelope.of(
                        ApiVersion.V1,
                        ProductRestMapper.toProductResponse(createProductUseCase.execute(ProductRestMapper.toDomain(request)))
                ));
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<ApiEnvelope<ProductResponse>> update(@RequestBody @Valid ProductUpdateRequest request, @PathVariable Long id) {

        UpdateProductCommand command = new UpdateProductCommand(
                request.name(),
                request.description(),
                request.price(),
                request.stock()
        );

        return ResponseEntity.ok(ApiEnvelope.of(
                ApiVersion.V1,
                ProductRestMapper.toProductResponse(updateProductUseCase.execute(command, id))
        ));
    }

    @Override
    @PatchMapping("/archive/{id}")
    public ResponseEntity<Void> archive(@PathVariable Long id) {
        archiveProductUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/desarchive/{id}")
    public ResponseEntity<Void> desarchive(@PathVariable Long id) {
        desarchiveProductUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

}
