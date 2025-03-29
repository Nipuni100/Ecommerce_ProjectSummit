package com.projectsummit.Carts_service.DTOs;

public record CartItemDTO(
        int cartItemID,
        String prodName,
        Float price,
        int Quantity

) {
}
