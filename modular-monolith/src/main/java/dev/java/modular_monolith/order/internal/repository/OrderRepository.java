package dev.java.modular_monolith.order.internal.repository;

import dev.java.modular_monolith.order.internal.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
