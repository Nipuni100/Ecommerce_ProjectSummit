package com.projectsummit.Order_service.Entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )

    private Long orderId;
    private Long customId;
    private String paymentMethod;
    private Integer numOfItems;
    private Integer totalPrice;
    private String orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> itemsList = new ArrayList<>();

    public Order() {
    }

    public Order(Long customId, String paymentMethod, Integer numOfItems, Integer totalPrice, String orderStatus, List<OrderItem> itemsList) {
        this.customId = customId;
        this.paymentMethod = paymentMethod;
        this.numOfItems = numOfItems;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.itemsList = itemsList;
    }

    public Order(Long orderId, Long customId, String paymentMethod, Integer numOfItems, Integer totalPrice, String orderStatus, List<OrderItem> itemsList) {
        this.orderId = orderId;
        this.customId = customId;
        this.paymentMethod = paymentMethod;
        this.numOfItems = numOfItems;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.itemsList = itemsList;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomId() {
        return customId;
    }

    public void setCustomId(Long customId) {
        this.customId = customId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getNumOfItems() {
        return numOfItems;
    }

    public void setNumOfItems(Integer numOfItems) {
        this.numOfItems = numOfItems;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
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
