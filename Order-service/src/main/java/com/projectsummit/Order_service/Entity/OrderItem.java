package com.projectsummit.Order_service.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="orderItems")
public class OrderItem {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )

    private Long orderItemId;
    private String prodName;
    private Long price;
    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    public OrderItem() {
    }

    public OrderItem(Long orderItemId, String prodName, Long price, Order order) {
        this.orderItemId = orderItemId;
        this.prodName = prodName;
        this.price = price;
        this.order = order;
    }

    public OrderItem(Long price, String prodName, Order order) {
        this.price = price;
        this.prodName = prodName;
        this.order = order;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
