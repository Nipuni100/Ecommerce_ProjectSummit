package com.projectsummit.Carts_service.DTOs;

import java.util.List;

public record CartItemsStatusUpdateDTO(List<Integer> productIds, String status) {
}