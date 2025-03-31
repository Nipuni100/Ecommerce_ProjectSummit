package com.projectsummit.Carts_service.DTOs;
import java.util.List;

public record OrderRequestDTO(
        String paymentMethod,
        String orderStatus,
        List<Integer> cartItemIds
) {
}

