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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ImplProductsService implements ProductsServiceInterface {
    private final ProductsRepository productsRepository;
    private final CategoryRepository categoryRepository;
    private final ProductResponseDTOMapper productResponseDTOMapper;
    private final ProductRequestDTOMapper productRequestDTOMapper;
    private final CategoryResponseDTOMapper categoryResponseDTOMapper;

    private static final Logger logger = LoggerFactory.getLogger(ImplProductsService.class);
    //Methods

    public Page<ProductResponseDTO> getProducts(String type, String searchQuery, Pageable pageable) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Search query cannot be empty.");
        }

        Page<Product> productsPage;

        logger.info("Searching by : " + type + " " + searchQuery);

        if ("product".equalsIgnoreCase(type)) {
            productsPage = productsRepository.findByProdNameContainingIgnoreCase(searchQuery, pageable);
        } else if ("category".equalsIgnoreCase(type)) {
            Category category = categoryRepository.findByCategoryName(searchQuery);
            if (category != null) {
                productsPage = productsRepository.findByCategoryId(category.getCategoryId(), pageable);
            } else {
                productsPage = Page.empty(pageable); // Return empty page
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid search type. Use 'product' or 'category'.");
        }
        return productsPage.map(productResponseDTOMapper);
    }


    public ProductResponseDTO getProductById(int prodId) {
        Product product = productsRepository.findById((long) prodId)
                .orElseThrow(() -> new NoSuchElementException("Product with ID " + prodId + " not found"));

        return productResponseDTOMapper.apply(product); // Assuming your mapper is a Function<Product, ProductResponseDTO>
    }


    public void removeProduct(int prodId) {
        boolean exists = productsRepository.existsById((long)prodId);
        if (!exists) {
            throw new IllegalStateException("Product with id " + prodId + " does not exist!");
        }
        productsRepository.deleteById((long)prodId);

    }


public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO) {
    // Validate category existence
    Category category = categoryRepository.findById((long)productRequestDTO.categoryId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Category not found with ID: " + productRequestDTO.categoryId()));

    // Map DTO to Entity
    Product product = new Product();
    product.setProdName(productRequestDTO.prodName());
    product.setBrand(productRequestDTO.brand());
    product.setCategoryId(category.getCategoryId());
    product.setSupplierId(productRequestDTO.supplierId());  // No need for supplier validation
    product.setPrice(productRequestDTO.price());
    product.setStatus(productRequestDTO.status());
    product.setStockCount(productRequestDTO.stockCount());

    // Save the product
    Product savedProduct = productsRepository.save(product);

    // Convert to Response DTO
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


    public Page<CategoryResponseDTO> getAllCategories(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(categoryResponseDTOMapper); // If this is a Function<Category, CategoryResponseDTO>
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
        if (categoryRepository.existsById((long)categoryId)) {
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

    public boolean existsById(int categoryId) {
        return categoryRepository.existsById((long)categoryId);
    }

    public boolean categoryExistsByName(String categoryName) {
        return categoryRepository.existsBycategoryNameIgnoreCase(categoryName);
    }
}
