package com.projectSummit.product_service.DTOs;

import org.springframework.lang.NonNull;

public record CategoryRequestDTO(
        @NonNull
        int categoryID,
        String categoryName,
        String categoryDescription

) {
}
