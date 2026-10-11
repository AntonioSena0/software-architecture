package dev.java.clean_architecture.domain.entity;

import dev.java.clean_architecture.domain.enums.ProductStatus;
import dev.java.clean_architecture.domain.valueobject.Name;
import dev.java.clean_architecture.domain.valueobject.Price;
import dev.java.clean_architecture.domain.valueobject.ProductId;
import dev.java.clean_architecture.domain.valueobject.Stock;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductModel {

    private ProductId id;
    private Name name;
    private String description;
    private Price price;
    private Stock stock;
    private ProductStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductModel(ProductId id, Name name, String description, Price price, Stock stock, ProductStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description =  (description == null || description.isEmpty()) ? "Não possui descrição" : description;;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ProductModel createNew(String name, String description, BigDecimal price, Integer stock) {

        Stock stockObj = new Stock(stock);
        ProductStatus initialStatus = (stockObj.value() <= 0) ? ProductStatus.INACTIVE : ProductStatus.ACTIVE;

        return new ProductModel(
                null,
                new Name(name),
                description,
                new Price(price),
                stockObj,
                initialStatus,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

    }

    public void updateDetails(Name name, String description, Price price, Stock stock) {

        ProductStatus status = (stock.value() <= 0) ? ProductStatus.INACTIVE : ProductStatus.ACTIVE;

        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.updatedAt = LocalDateTime.now();

    }

    public void archive(){
        this.status = ProductStatus.ARCHIVED;
    }

    public void desarchive(Stock stock){
        if(stock.value() <= 0){
            this.status = ProductStatus.INACTIVE;
        } else {
            this.status = ProductStatus.ACTIVE;
        }
    }

    public ProductId getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Price getPrice() {
        return price;
    }

    public Stock getStock() {
        return stock;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}
