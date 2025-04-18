package com.projectsummit.Order_service.DTOs;

public record OrderItemDTO(
        int orderItemId,
        String prodName,
        Float price,
        int quantity
) {
}
