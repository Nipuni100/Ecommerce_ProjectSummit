package com.projectSummit.product_service.Mappers;

import com.projectSummit.product_service.DTOs.ProductRequestDTO;
import com.projectSummit.product_service.DTOs.ProductResponseDTO;
import com.projectSummit.product_service.Entity.Product;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProductRequestDTOMapper implements Function<ProductRequestDTO, Product> {
    @Override
    public Product apply(ProductRequestDTO dto) {
        return new Product(
                dto.prodId(),
                dto.prodName(),
                dto.brand(),
                dto.categoryId(),
                dto.supplierId(),
                dto.price(),
                dto.stockCount(),
                dto.status()

        );

    }

}
