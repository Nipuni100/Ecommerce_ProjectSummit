package com.projectsummit.Order_service.DTOs;

public record ProductDTOforOrder(
        int prodId,
        String prodName,
        Float price,
        int quantity

) {
}
