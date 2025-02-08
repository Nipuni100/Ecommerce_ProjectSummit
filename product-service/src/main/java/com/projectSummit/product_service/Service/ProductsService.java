package com.projectSummit.product_service.Service;

import com.projectSummit.product_service.DTOs.ProductRequestDTO;
import com.projectSummit.product_service.DTOs.ProductResponseDTO;
import com.projectSummit.product_service.Entity.Category;
import com.projectSummit.product_service.Entity.Product;
import com.projectSummit.product_service.Repository.CategoryRepository;
import com.projectSummit.product_service.Repository.ProductsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductsService implements ProductsServiceInterface {
    private final ProductsRepository productsRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductsService(ProductsRepository productsRepository, CategoryRepository categoryRepository,ModelMapper modelMapper) {
        this.productsRepository = productsRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Autowired
    private ModelMapper modelMapper;

    Product product = new Product();
    Category category = new Category();
    ProductResponseDTO productResponseDTO = modelMapper.map(product, ProductResponseDTO.class);
    ProductRequestDTO productRequestDTO = modelMapper.map(product, ProductRequestDTO.class);

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

//    public Page<Product> getProductByName(String prodName, Pageable pageable) {
//        return productsRepository.findByNameContaining(prodName, pageable);
//    }
    public List<ProductResponseDTO> getProduct(String type, String searchQuery) {
//        Check the type as well, category or product
//        if category this elif this
        List<Product> products;

        if ("product".equalsIgnoreCase(type)) {
            // Search by product name
            products = productsRepository.findByProdName(searchQuery);
        } else if ("category".equalsIgnoreCase(type)) {
            // Search by category name
            Category category = categoryRepository.findByCategoryName(searchQuery);
            products = (category != null) ? productsRepository.findByCategory(category) : new ArrayList<>();
        } else {
            throw new IllegalArgumentException("Invalid search type. Use 'product' or 'category'.");
        }


        return products
                .stream()
                .map(product ->productResponseDTO)
                .collect(Collectors.toList());
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category addNewCategory(Category category) {
         categoryRepository.save(category);
        return category;
    }


    public boolean deleteCategory(Long categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
            return true;
        } else {
            return false;

        }
    }


    public Category updateCategory(int categoryId, Category category) {
        if (categoryRepository.existsById(categoryId)) {
//            category.setCategoryId(categoryId);
            return categoryRepository.save(category);
        } else {
            return null;
        }
    }

    public Product addProduct(Product product) {
        return productsRepository.save(product);
    }


    public Product updateProductStatus(int prodId, String status) {
        return productsRepository.findById((long)prodId).map(product -> {
            product.setStatus(status);
            return productsRepository.save(product);
        }).orElse(null);
    }
}
