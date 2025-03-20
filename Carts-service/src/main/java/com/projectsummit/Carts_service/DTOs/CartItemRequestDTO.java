package com.projectsummit.Carts_service.DTOs;

public record CartItemRequestDTO (
        int cartItemId,
        int cartId,
        String prodName,
        Float price,
        int quantity,
        String status
){
}
