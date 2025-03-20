package com.projectsummit.Carts_service.DTOs;

import java.util.List;

public record CartResponseDTO(
        int cartId,
        int customerId,
        List<CartItemResponseDTO> cartItems
) {
}
