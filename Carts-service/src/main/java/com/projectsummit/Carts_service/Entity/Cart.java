package com.projectsummit.Carts_service.Entity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Cart {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )

    private Long cartId;
    private Long customId;
    @OneToMany(mappedBy = "cartItems", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> Items = new ArrayList<>();
}
