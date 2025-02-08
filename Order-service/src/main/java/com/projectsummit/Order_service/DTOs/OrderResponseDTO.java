package com.projectsummit.Order_service.DTOs;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public record OrderResponseDTO(
        @NonNull
        Long orderId,
        @NonNull
        Long customerId,
        String paymentMethod,
        @Nullable
        Integer numOfItems,
        Integer totalPrice,
        String orderStatus
) {

}
