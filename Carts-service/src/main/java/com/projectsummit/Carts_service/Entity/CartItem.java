package com.projectsummit.Carts_service.Entity;

import jakarta.persistence.*;

@Entity
@Table
public class CartItem {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long cartItemId;
    private String prodName;
    private Integer Price;
    private String brand;
    private Integer Qty;
    private String status;

    public CartItem() {
    }

    public CartItem(String prodName, Integer price, String brand, Integer qty, String status) {
        this.prodName = prodName;
        Price = price;
        this.brand = brand;
        Qty = qty;
        Status = status;
    }

    public CartItem(Long cartItemId, String prodName, Integer price, String brand, String status, Integer qty) {
        this.cartItemId = cartItemId;
        this.prodName = prodName;
        Price = price;
        this.brand = brand;
        Status = status;
        Qty = qty;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public Integer getPrice() {
        return Price;
    }

    public void setPrice(Integer price) {
        Price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getQty() {
        return Qty;
    }

    public void setQty(Integer qty) {
        Qty = qty;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
