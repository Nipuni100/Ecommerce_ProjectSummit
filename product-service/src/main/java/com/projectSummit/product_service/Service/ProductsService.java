package com.projectSummit.product_service.Service;


import com.projectSummit.product_service.DTOs.CategoryRequestDTO;
import com.projectSummit.product_service.DTOs.CategoryResponseDTO;
import com.projectSummit.product_service.DTOs.ProductRequestDTO;
import com.projectSummit.product_service.DTOs.ProductResponseDTO;
import com.projectSummit.product_service.Entity.Category;
import com.projectSummit.product_service.Entity.Product;
import com.projectSummit.product_service.Mappers.CategoryResponseDTOMapper;
import com.projectSummit.product_service.Mappers.ProductRequestDTOMapper;
import com.projectSummit.product_service.Mappers.ProductResponseDTOMapper;
import com.projectSummit.product_service.Repository.CategoryRepository;
import com.projectSummit.product_service.Repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductsService implements ProductsServiceInterface {
    private final ProductsRepository productsRepository;
    private final CategoryRepository categoryRepository;
    private final ProductResponseDTOMapper productResponseDTOMapper;
    private final ProductRequestDTOMapper productRequestDTOMapper;
    private final CategoryResponseDTOMapper categoryResponseDTOMapper;

    //Methods

    public List<ProductResponseDTO> getProducts(String type, String searchQuery) {
        List<Product> products;
        if ("product".equalsIgnoreCase(type)) {
            products = productsRepository.findByProdName(searchQuery);
        } else if ("category".equalsIgnoreCase(type)) {
            Category category = categoryRepository.findByCategoryName(searchQuery);  //Return CategoryId
            if (category != null) {
                int categoryId = category.getCategoryId();
                products = productsRepository.findByCategoryId(categoryId); // Fetch products using categoryId
            } else {
                products = new ArrayList<>();
            }
        } else {
            throw new IllegalArgumentException("Invalid search type. Use 'product' or 'category'.");
        }

        return products
                .stream()
                .map(productResponseDTOMapper)
                .collect(Collectors.toList());
    }


    public Product getProductById(int prodId) {
        return productsRepository.findById((long)prodId)
                .orElseThrow(() -> new NoSuchElementException("Product with ID " + prodId + " not found"));
    }

    public void removeProduct(int prodId) {
        boolean exists = productsRepository.existsById((long)prodId);
        if (!exists) {
            throw new IllegalStateException("Product with id " + prodId + " does not exist!");
        }
        productsRepository.deleteById((long)prodId);

    }


    public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO) {
        Product product = productRequestDTOMapper.apply(productRequestDTO);
        Product savedProduct = productsRepository.save(product);
        return productResponseDTOMapper.apply(savedProduct);
    }


    public Product updateProductStatus(int prodId, String status) {
        Optional<Product> optionalProduct = productsRepository.findById((long)prodId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setStatus(status);
            return productsRepository.save(product);
        }
        return null;
    }

    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryResponseDTOMapper)
                .collect(Collectors.toList());
    }


    public CategoryResponseDTO getCategoryById(int categoryId)
    {
        Optional<Category> categoryOptional = categoryRepository.findById((long)categoryId);
        return categoryOptional.map(
                        categoryResponseDTOMapper)
                .orElse(null);
    }


    public Category addNewCategory(Category category) {
        categoryRepository.save(category);
        return category;
    }

    public boolean deleteCategory(int categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById((long)categoryId);
            return true;
        } else {
            return false;
        }
    }


    public CategoryResponseDTO updateCategory(int categoryId, CategoryRequestDTO categoryRequestDTO) {

        Optional<Category> categoryOptional = categoryRepository.findById((long)categoryId);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setCategoryName(categoryRequestDTO.categoryName());
            category.setCategoryDescription(categoryRequestDTO.categoryDescription());
            Category updatedCategory = categoryRepository.save(category);

            return categoryResponseDTOMapper.apply(updatedCategory);
        } else {
            return null;
        }
    }


}
