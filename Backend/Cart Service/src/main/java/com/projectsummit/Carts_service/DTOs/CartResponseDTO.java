package com.projectsummit.Carts_service.DTOs;

import org.springframework.lang.NonNull;

import java.util.List;

public record CartResponseDTO(
        @NonNull
        int cartId,
        int customerId,
        List<CartItemDTO> cartItems
) {
}
