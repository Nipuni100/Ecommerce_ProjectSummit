package com.projectsummit.Carts_service.DTOs;

import java.util.List;

public record CartRequestDTO(
        int customerId,
        String status,
        List<CartItemRequestDTO> cartItems
) {
}

