package com.projectSummit.product_service.Service;

import com.projectSummit.product_service.DTOs.CategoryRequestDTO;
import com.projectSummit.product_service.DTOs.CategoryResponseDTO;
import com.projectSummit.product_service.DTOs.ProductRequestDTO;
import com.projectSummit.product_service.DTOs.ProductResponseDTO;
import com.projectSummit.product_service.Entity.Category;
import com.projectSummit.product_service.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductsServiceInterface {

    Page<ProductResponseDTO> getProducts(String type, String searchQuery, Pageable pageable);

    ProductResponseDTO getProductById(int prodId);

    ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO);

    void removeProduct(int prodId);

    Product updateProductStatus(int prodId, String status);

    Page<CategoryResponseDTO> getAllCategories(Pageable pageable);

    CategoryResponseDTO getCategoryById(int categoryId);

    Category addNewCategory(Category category);

    boolean deleteCategory(int categoryId);

    CategoryResponseDTO updateCategory(int categoryId, CategoryRequestDTO categoryRequestDTO);
}
