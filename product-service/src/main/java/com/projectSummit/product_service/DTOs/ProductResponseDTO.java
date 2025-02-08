package com.projectSummit.product_service.DTOs;

//import jakarta.persistence.Embeddable;


import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public record ProductResponseDTO (
        @NonNull
        int prodId,
        String prodName,
        Float Price,
        @Nullable
        int StkCount
){


}