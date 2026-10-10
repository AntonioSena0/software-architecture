package dev.java.modular_monolith.product;

import java.math.BigDecimal;

public interface ProductManager {

    Integer getStock(Long id);
    BigDecimal getProductPrice(Long id);
    void checkArchived(Long id);
    void decreaseStock(Long id, Integer quantity);

}
