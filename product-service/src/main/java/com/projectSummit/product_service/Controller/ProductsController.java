package com.projectSummit.product_service.Controller;

import com.projectSummit.product_service.DTOs.*;
import com.projectSummit.product_service.Entity.Category;
import com.projectSummit.product_service.Entity.Product;
import com.projectSummit.product_service.ExceptionHandling.*;
import com.projectSummit.product_service.Service.ImplProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path="/api/v1/products")
public class ProductsController {
    private final ImplProductsService productsService;
    private static final Logger logger = LoggerFactory.getLogger(ProductsController.class);

    @Autowired
    public ProductsController(ImplProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping // Searching for products by category and productName
    public ResponseEntity<Page<ProductResponseDTO>> getProducts(
            @RequestParam String type,
            @RequestParam String searchQuery,
            Pageable pageable) {

        logger.info("Request received for page number: {}, page size: {}", pageable.getPageNumber(), pageable.getPageSize());


        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            logger.error("Search query is empty or missing");
            throw new EmptySearchQueryException("searchQuery is required and cannot be empty.");
        }

        if (!type.equalsIgnoreCase("product") && !type.equalsIgnoreCase("category")) {
            logger.error("Invalid 'type' parameter: {}", type);
            throw new InvalidTypeException("Invalid 'type' parameter. Allowed values are 'product' or 'category'.");

        }

        Page<ProductResponseDTO> products = productsService.getProducts(type, searchQuery, pageable);
        logger.info("Successfully fetched {} products for type '{}', page number: {}, page size: {}",
                products.getTotalElements(), type, pageable.getPageNumber(), pageable.getPageSize());

        return ResponseEntity.ok(products);
    }

    @PostMapping // Add a product to the inventory
    public ResponseEntity<ProductResponseDTO> addProduct(@RequestBody ProductRequestDTO productRequestDTO) {

        logger.info("Received request to add product: {}", productRequestDTO.prodName());
        try {
            if (!productsService.existsById(productRequestDTO.categoryId())) {
                logger.warn("Category with ID {} does not exist.", productRequestDTO.categoryId());
                throw new InvalidCategoryIdException("Category with ID " + productRequestDTO.categoryId() + " does not exist.");
            }


            ProductResponseDTO savedProduct = productsService.addProduct(productRequestDTO);
            logger.info("Product successfully added with ID: {}", savedProduct.prodId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
//        } catch (ProductAlreadyExistsException e) {
//            logger.warn("Product already exists: {}", e.getMessage());
//            throw e;
//        } catch (InvalidProductIdException e) {
//            logger.error("Invalid Product ID: {}", e.getMessage());
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);

        } catch (Exception e) {
        logger.error("Unexpected server error: {}", e.getMessage(), e);
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", e);
        }
    }


    @GetMapping("/{prodId}")
    public ProductResponseDTO getProductById(@PathVariable int prodId) {

        try {
            logger.info("Fetching product with ID: {}", prodId);

            if (prodId <= 0) {
                throw new InvalidProductIdException("Invalid Product ID: " + prodId);
            }

            ProductResponseDTO product = productsService.getProductById(prodId);
            logger.info("Successfully fetched product with ID: {}", prodId);

            return productsService.getProductById(prodId);

        } catch (ProductNotFoundException e) {
            logger.warn("Product not found for ID: {}", prodId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);}


    }


    @DeleteMapping("/{prodId}") //Delete a product
    public ResponseEntity<String> removeProduct(@PathVariable int prodId) {
        try {

            logger.info("Received request to delete product with ID: {}", prodId);
            if (prodId <= 0) {
                throw new InvalidProductIdException("Invalid Product ID: " + prodId);
            }

            productsService.removeProduct(prodId);
            logger.info("Product with ID {} deleted successfully.", prodId);
            return ResponseEntity.ok("Product with ID " + prodId + " deleted successfully.");

        } catch (ProductNotFoundException e) {
            logger.warn("Failed to delete product. Reason: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);

        }
    }

    @PatchMapping("/{prodId}") //Change the product Status
    public ResponseEntity<Product> updateProductStatus(
            @PathVariable int prodId,
            @RequestBody ProductStatusDTO statusDTO)
    {
        try {
            logger.info("Received request to update status for product with ID: {} to status: {}", prodId, statusDTO.status());

            String status = statusDTO.status().trim();

            Product updatedProduct = productsService.updateProductStatus(prodId, status);
            logger.info("Successfully updated product with ID {} to status: {}", prodId, status);
            return ResponseEntity.ok(updatedProduct);
        }
        catch (ProductNotFoundException e) {
            logger.warn("Failed to update status. Product with ID {} not found.", prodId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);

        }catch (Exception e) {
            logger.error("Unexpected error occurred while updating status for product with ID {}: {}", prodId, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", e);
        }

    }

    @GetMapping("/categories")
    public ResponseEntity<Page<CategoryResponseDTO>> getAllCategories(Pageable pageable) {
        try {

            logger.info("Received request to fetch all categories with pagination: {}", pageable);

            Page<CategoryResponseDTO> categoryPage = productsService.getAllCategories(pageable);

            if (categoryPage.isEmpty()) {
                logger.warn("No categories found for the provided pagination: {}", pageable);
                return ResponseEntity.noContent().build();
            }

            logger.info("Successfully fetched {} categories.", categoryPage.getTotalElements());
            return ResponseEntity.ok(categoryPage);

        }
     catch (Exception e) {
        logger.error("Unexpected error occurred while fetching categories: {}", e.getMessage(), e);
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", e);
    }

    }


    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        logger.info("Received request to add new category: {}", category.getCategoryName());
        try {
        if (productsService.categoryExistsByName(category.getCategoryName())) {
            logger.warn("Category with name '{}' already exists.", category.getCategoryName());
            throw new CategoryAlreadyExistsException("Category with name '" + category.getCategoryName() + "' already exists.");
        }

        Category savedCategory = productsService.addNewCategory(category);
        logger.info("Category successfully added with ID: {}", savedCategory.getCategoryId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        } catch (Exception e) {
            logger.error("Unexpected server error while adding category: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", e);
        }
    }

    @GetMapping("/categories/{categoryId}") // Get category by categoryId
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable int categoryId) {
        logger.info("Received request to fetch category with ID: {}", categoryId);
        try{
        CategoryResponseDTO categoryResponseDTO = productsService.getCategoryById(categoryId);
        if (categoryResponseDTO != null) {
            logger.info("Category found with ID: {}", categoryId);

            return ResponseEntity.ok(categoryResponseDTO);
        } else {
            logger.warn("Category with ID {} not found.", categoryId);
            throw new CategoryIDNotFoundException("Category with ID " + categoryId + " not found.");

        }} catch (CategoryIDNotFoundException e) {
            logger.error("Error fetching category: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected server error while fetching category: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", e);
        }

    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable int categoryId) {
        logger.info("Received request to delete category with ID: {}", categoryId);
        try{
        boolean isDeleted = productsService.deleteCategory(categoryId);
        if (isDeleted) {
            logger.info("Category with ID {} deleted successfully.", categoryId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Category deleted successfully");
        } else {
            logger.warn("Category with ID {} not found.", categoryId);
            throw new CategoryIDNotFoundException("Category with ID " + categoryId + " not found.");
        } }
        catch (CategoryIDNotFoundException e) {
            logger.error("Error deleting category: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);}
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable int categoryId, @RequestBody CategoryRequestDTO categoryRequestDTO) {
        logger.info("Received request to update category with ID: {}", categoryId);
        try {
            CategoryResponseDTO updatedCategory = productsService.updateCategory(categoryId, categoryRequestDTO);


            if (updatedCategory != null) {
                logger.info("Category with ID {} updated successfully.", categoryId);
                return ResponseEntity.ok(updatedCategory);
            } else {
                logger.warn("Category with ID {} not found for update.", categoryId);
                throw new CategoryIDNotFoundException("Category with ID " + categoryId + " not found.");
            }
        } catch (CategoryIDNotFoundException e) {
            logger.error("Error updating category: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }}

