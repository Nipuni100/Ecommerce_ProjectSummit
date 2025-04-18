package com.projectsummit.Carts_service.DTOs;

public record CartItemResponseDTO(
        int cartItemId,
        String prodName,
        Float price,
        int quantity,
        String status
) {
}
