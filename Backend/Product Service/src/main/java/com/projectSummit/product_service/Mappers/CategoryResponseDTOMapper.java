package com.projectSummit.product_service.Mappers;

import com.projectSummit.product_service.DTOs.CategoryResponseDTO;
import com.projectSummit.product_service.DTOs.ProductRequestDTO;
import com.projectSummit.product_service.Entity.Category;
import com.projectSummit.product_service.Entity.Product;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CategoryResponseDTOMapper implements Function <Category, CategoryResponseDTO> {
    @Override
    public CategoryResponseDTO apply(Category category) {
        return new CategoryResponseDTO(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getCategoryDescription()
                )
                ;

    }

}
