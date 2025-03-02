package com.projectSummit.product_service.DTOs;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public record ProductRequestDTO (
        @NonNull
        int prodId,
        String prodName,
        String brand,
        Float price,
        @Nullable
        int stockCount

){}
