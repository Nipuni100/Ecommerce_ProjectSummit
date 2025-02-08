package com.projectSummit.product_service.Repository;

import com.projectSummit.product_service.DTOs.ProductResponseDTO;
import com.projectSummit.product_service.Entity.Category;
import com.projectSummit.product_service.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository
        extends JpaRepository<Product, Long> {
    List<Product> findByprodName(String prodName);

    List<Product> findByProdName(String prodName);

    List<Product> findByCategory(Category category);
//    Page<Product> findByNameContaining(String prodName, Pageable pageable);

}


