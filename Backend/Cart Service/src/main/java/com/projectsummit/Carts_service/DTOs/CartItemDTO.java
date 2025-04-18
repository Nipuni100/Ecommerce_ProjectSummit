package com.projectsummit.Carts_service.DTOs;

import lombok.NonNull;

public record CartItemDTO(
        @NonNull
        int cartItemID,
        String prodName,
        Float price,
        int Quantity,
        @NonNull
        int prodId

) {
}
