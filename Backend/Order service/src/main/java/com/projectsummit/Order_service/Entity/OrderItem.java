package com.projectsummit.Order_service.Entity;

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
    private int prodId;

    public OrderItem() {
    }

    public OrderItem(int orderItemId, String prodName, Float price, int quantity, int orderId, int prodID) {
        this.orderItemId = orderItemId;
        this.prodName = prodName;
        this.price = price;
        this.quantity = quantity;
        this.orderId = orderId;
        this.prodId = prodID;
    }

    public OrderItem(String prodName, Float price, int quantity, int orderId, int prodId) {
        this.prodName = prodName;
        this.price = price;
        this.quantity = quantity;
        this.orderId = orderId;
        this.prodId = prodId;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdID(int prodID) {
        this.prodId = prodID;
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

