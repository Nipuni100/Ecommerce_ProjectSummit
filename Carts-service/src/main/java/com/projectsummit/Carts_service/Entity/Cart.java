package com.projectsummit.Carts_service.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private int customId;
//    @OneToMany(mappedBy = "cartId", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<CartItem> Items = new ArrayList<>();

    public Cart() {
    }

    public Cart(int customId) {
        this.customId = customId;

    }

    public Cart(int cartId, int customId) {
        this.cartId = cartId;
        this.customId = customId;

    }
}
