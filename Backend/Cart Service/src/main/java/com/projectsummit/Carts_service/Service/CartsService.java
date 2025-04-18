package com.projectsummit.Carts_service.Service;

import com.projectsummit.Carts_service.DTOs.CartItemRequestDTO;
import com.projectsummit.Carts_service.DTOs.CartResponseDTO;
import com.projectsummit.Carts_service.Entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartsService {

    // CART METHODS

    CartResponseDTO getCartById(int cartId);

    CartResponseDTO getCartByCustomerId(int customerId);

    void addItemToCart(CartItemRequestDTO cartItemRequestDTO, int customerId);

    void removeItemFromCart(int customerId, int itemId);

    CartItem updateItemQuantity(int customerId, int itemId, int newQuantity);

    void emptyCart(int customerId);

    Page<CartResponseDTO> getAllCarts(Pageable pageable);
}
