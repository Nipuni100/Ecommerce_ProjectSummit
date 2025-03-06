package com.projectsummit.Carts_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table
@Setter
@Getter
public class CartItem {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int cartItemId;
    private String prodName;
    private Float price;
    private String brand;
    private int quantity;
    private String status;

    public CartItem() {
    }

    public CartItem(String prodName, Float price, String brand, int quantity, String status) {
        this.prodName = prodName;
        this.price = price;
        this.brand = brand;
        this.quantity = quantity;
        this.status = status;
    }

    public CartItem(String prodName, int cartItemId, Float price, String brand, int quantity, String status) {
        this.prodName = prodName;
        this.cartItemId = cartItemId;
        this.price = price;
        this.brand = brand;
        this.quantity = quantity;
        this.status = status;
    }
}
