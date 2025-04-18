package com.projectSummit.product_service.DTOs;

import org.springframework.lang.NonNull;

public record CategoryResponseDTO(
        @NonNull
        int categoryID,
        String CategoryName,
        String CategoryDescription
) {
}
