package com.projectSummit.product_service.Mappers;

import com.projectSummit.product_service.DTOs.ProductResponseDTO;
import com.projectSummit.product_service.Entity.Product;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProductResponseDTOMapper implements Function<Product , ProductResponseDTO> {
    @Override
    public ProductResponseDTO apply(Product product) {
        return new ProductResponseDTO(
                product.getProdId(),
                product.getCategoryId(),
                product.getProdName(),
                product.getPrice(),
                product.getStockCount())
                ;

    }

}
