package com.projectSummit.product_service.Mappers;

import com.projectSummit.product_service.DTOs.ProductRequestDTO;
import com.projectSummit.product_service.Entity.Product;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CategoryRequestDTOMapper implements Function<Product , ProductRequestDTO> {
    @Override
    public ProductRequestDTO apply(Product product) {
        return new ProductRequestDTO(
                product.getProdId(),
                product.getProdName(),
                product.getBrand(),
                product.getPrice(),
                product.getStockCount()
                )
                ;

    }

}
