package dev.java.layered_architecture.service;

import dev.java.layered_architecture.dto.request.ProductRequest;
import dev.java.layered_architecture.dto.request.ProductUpdateRequest;
import dev.java.layered_architecture.dto.response.ProductResponse;
import dev.java.layered_architecture.entity.Product;
import dev.java.layered_architecture.exception.ProductAlreadyExists;
import dev.java.layered_architecture.exception.ProductNotFound;
import dev.java.layered_architecture.mapper.ProductMapper;
import dev.java.layered_architecture.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ProductMapper::toProductResponse);
    }

    @Override
    public ProductResponse findById(Long id) {
        return ProductMapper.toProductResponse(repository.findById(id)
                .orElseThrow(() -> new ProductNotFound("Produto não encontrado")));
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {

        if(repository.existsByName(request.name())){
            throw new ProductAlreadyExists("Esse produto já existe");
        }

        return ProductMapper.toProductResponse(repository.save(ProductMapper.toProduct(request)));

    }

    @Override
    @Transactional
    public ProductResponse update(ProductUpdateRequest request, Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFound("Produto não encontrado"));

        if(request.name() != null){

            String name = request.name()
                            .trim()
                            .toLowerCase()
                            .replaceAll("\\s+", " ");

            if(repository.existsByNameAndIdNot(name, id)){
                throw new ProductAlreadyExists("Esse produto já está cadastrado");
            }

            product.setName(name);

        }

        if(request.description() != null){
            product.setDescription(request.description());
        }

        if(request.price() != null){
            product.setPrice(request.price());
        }

        if(request.stock() != null){
            product.setStock(request.stock());
        }

        return ProductMapper.toProductResponse(product);

    }

    @Override
    @Transactional
    public void delete(Long id) {

        if(!repository.existsById(id)){
            throw new ProductNotFound("Produto não encontrado");
        }

        repository.deleteById(id);

    }

}