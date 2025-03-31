package com.projectsummit.Carts_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class OrderItem {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int orderItemId;
    private String prodName;
    private Float price;
    private int quantity;
    private int orderId;

    public OrderItem() {
    }


    public OrderItem(String prodName, Float price, int quantity, int orderId) {
        this.prodName = prodName;
        this.price = price;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public OrderItem(int orderItemId, String prodName, Float price, int quantity, int orderId) {
        this.orderItemId = orderItemId;
        this.prodName = prodName;
        this.price = price;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}

