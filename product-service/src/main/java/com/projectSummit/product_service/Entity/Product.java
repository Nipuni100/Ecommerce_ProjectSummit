package com.projectSummit.product_service.Entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Getter
@Setter
@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int prodId;
    private String prodName;
    private String brand;

    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @Column(name = "category_id", nullable = false)
    private int categoryId;

    private int supplierId;
    private Float price;
    private int stockCount;
    private String status;

    public Product() {
    }

    public Product(String prodName, String brand, int categoryId, int supplierId, Float price, String status, int stockCount) {
        this.prodName = prodName;
        this.brand = brand;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.price = price;
        this.status = status;
        this.stockCount = stockCount;
    }

    public Product(int prodId, String prodName, String brand, int categoryId, int supplierId, Float price, int stockCount, String status) {
        this.prodId = prodId;
        this.prodName = prodName;
        this.brand = brand;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.price = price;
        this.stockCount = stockCount;
        this.status = status;
    }

}
