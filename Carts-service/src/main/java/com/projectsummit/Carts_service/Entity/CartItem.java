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
    private int cartId;
    private String prodName;
    private Float price;
    private String brand;
    private int quantity;
    private String status;

    public CartItem() {
    }

    public CartItem(int cartId, String prodName, Float price, String brand, int quantity, String status) {
        this.cartId = cartId;
        this.prodName = prodName;
        this.price = price;
        this.brand = brand;
        this.quantity = quantity;
        this.status = status;
    }

    public CartItem(int cartItemId, int cartId, Float price, String prodName, String brand, int quantity, String status) {
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.price = price;
        this.prodName = prodName;
        this.brand = brand;
        this.quantity = quantity;
        this.status = status;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
