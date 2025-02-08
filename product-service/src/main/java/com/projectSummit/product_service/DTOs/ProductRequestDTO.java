package com.projectSummit.product_service.DTOs;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public record ProductRequestDTO (
        @NonNull
        int prodId,
        String prodName,
        Float Price,
        @Nullable
        int StkCount,
        String brand
){




}
