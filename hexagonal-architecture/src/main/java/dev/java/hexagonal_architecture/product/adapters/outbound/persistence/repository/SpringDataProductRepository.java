package dev.java.hexagonal_architecture.product.adapters.outbound.persistence.repository;

import dev.java.hexagonal_architecture.product.adapters.outbound.persistence.entity.ProductJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProductRepository extends JpaRepository<ProductJpaEntity, Long> {

    Page<ProductJpaEntity> findAll(Pageable pageable);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

}
