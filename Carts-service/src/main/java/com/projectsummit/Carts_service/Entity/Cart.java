package com.projectsummit.Carts_service.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Setter
@Getter
public class Cart {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )

    private int cartId;
    private int customerId;
    private String status;
    @OneToMany(mappedBy = "cartId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> Items = new ArrayList<>();

    public Cart() {
    }

    public Cart(int customerId) {
        this.customerId = customerId;
    }

    public Cart(int cartId, int customerId) {
        this.cartId = cartId;
        this.customerId = customerId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CartItem> getItems() {
        return Items;
    }

    public void setItems(List<CartItem> items) {
        Items = items;
    }
}
