package com.projectsummit.Carts_service.Service;

import com.projectsummit.Carts_service.Entity.CartItem;
//import com.projectsummit.Carts_service.DTOs.CartItemDTO;
import com.projectsummit.Carts_service.Entity.Cart;
import com.projectsummit.Carts_service.Repository.CartsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartsService {
    private CartsRepository cartsRepository;

    @Autowired
    public CartsService(CartsRepository cartsRepository) {
        this.cartsRepository = cartsRepository;
    }


    public List<Cart> getAllCarts() {
        return cartsRepository.findAll();
    }

    public ResponseEntity<Cart> getOrderById(Long cartId) {
        Optional<Cart> cart = cartsRepository.findById(cartId);
        if (cart.isPresent()) {
            return ResponseEntity.ok(cart.get());
        }
        return ResponseEntity.notFound().build();
    }


    public CartItem addcartItem(CartItem cartItem) {
        cartsRepository.save(cartItem);
        return cartItem;
    }
}
