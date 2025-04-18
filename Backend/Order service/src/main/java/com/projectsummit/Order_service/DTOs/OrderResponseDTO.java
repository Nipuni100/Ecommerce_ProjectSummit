package com.projectsummit.Order_service.DTOs;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

public record OrderResponseDTO(
        @NonNull
        int orderId,
        @NonNull int customerId,
        String paymentMethod,
        Float totalPrice,
        String orderStatus,
        List<OrderItemDTO> orderItems
) {

}
