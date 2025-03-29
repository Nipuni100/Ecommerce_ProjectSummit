package com.projectsummit.Order_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )

    private int orderId;
    private int customerId;
    private String paymentMethod;
    private int numOfItems;
    private Float totalPrice;
    private String orderStatus;

    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> itemsList = new ArrayList<>();

    public Order() {
    }

    public Order(int orderId, int customerId, String paymentMethod, int numOfItems, Float totalPrice, String orderStatus, List<OrderItem> itemsList) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.paymentMethod = paymentMethod;
        this.numOfItems = numOfItems;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.itemsList = itemsList;
    }

    public Order(int customerId, String paymentMethod, int numOfItems, Float totalPrice, String orderStatus, List<OrderItem> itemsList) {
        this.customerId = customerId;
        this.paymentMethod = paymentMethod;
        this.numOfItems = numOfItems;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.itemsList = itemsList;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getNumOfItems() {
        return numOfItems;
    }

    public void setNumOfItems(int numOfItems) {
        this.numOfItems = numOfItems;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderItem> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<OrderItem> itemsList) {
        this.itemsList = itemsList;
    }
}


