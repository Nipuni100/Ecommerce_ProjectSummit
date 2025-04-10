package com.projectsummit.Order_service.DTOs;
import java.util.List;

public record OrderRequestDTO(
        int cartId,
        String paymentMethod,
        String orderStatus,

        List<ProductDTOforOrder> productList
) {
}

