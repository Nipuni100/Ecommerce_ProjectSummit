package com.projectsummit.Carts_service.DTOs;

import lombok.NonNull;

import java.util.List;

public record CartRequestDTO(
        @NonNull
        int customerId,
        String status,
        List<CartItemRequestDTO> cartItems
) {
}

