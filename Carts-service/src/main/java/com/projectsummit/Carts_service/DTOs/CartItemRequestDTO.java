package com.projectsummit.Carts_service.DTOs;

import org.springframework.lang.NonNull;

public record CartItemRequestDTO (
        @NonNull
        int cartItemId,
        @NonNull
        int cartId,
        String prodName,
        String brand,
        Float price,
        int quantity,
        String status,
        @NonNull
        int prodId
){
}
