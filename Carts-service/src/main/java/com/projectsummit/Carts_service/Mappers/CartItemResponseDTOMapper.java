package com.projectsummit.Carts_service.Mappers;

import com.projectsummit.Carts_service.DTOs.CartItemResponseDTO;
import com.projectsummit.Carts_service.Entity.CartItem;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CartItemResponseDTOMapper implements Function<CartItem, CartItemResponseDTO> {
    @Override
    public CartItemResponseDTO apply(CartItem cartItem) {
        return new CartItemResponseDTO(
                cartItem.getCartItemId(),
                cartItem.getProdName(),
                cartItem.getPrice(),
                cartItem.getQuantity(),
                cartItem.getStatus()
        );
    }
}
