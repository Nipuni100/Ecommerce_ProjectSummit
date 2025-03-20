package com.projectSummit.product_service.Controller;

import com.projectSummit.product_service.DTOs.ProductRequestDTO;
import com.projectSummit.product_service.DTOs.ProductResponseDTO;
import com.projectSummit.product_service.DTOs.ProductStatusDTO;
import com.projectSummit.product_service.Entity.Category;
import com.projectSummit.product_service.Entity.Product;
import com.projectSummit.product_service.Service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path="/api/v1/products")
public class ProductsController {
    private final ProductsService productsService;

    @Autowired
    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping //Searching for products by category and productName
    public ResponseEntity<List<ProductResponseDTO>> getProducts(
            @RequestParam String type,
            @RequestParam String searchQuery) {
        List<ProductResponseDTO> products = productsService.getProducts(type, searchQuery);
        return ResponseEntity.ok(products);
    }

    @PostMapping //Add a product to the inventory
    public ResponseEntity<ProductResponseDTO> addProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO savedProduct = productsService.addProduct(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @GetMapping("/{prodId}") //Get products by prodId
    public Product getProductById(@PathVariable int prodId) {
        return productsService.getProductById(prodId);
    }

    @DeleteMapping("/{prodId}") //Delete a product
    public ResponseEntity<String> removeProduct(@PathVariable int prodId) {
        try {
            productsService.removeProduct(prodId);
            return ResponseEntity.ok("Product with ID " + prodId + " deleted successfully.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{prodId}") //Change the product Status
    public ResponseEntity<Product> updateProductStatus(
            @PathVariable int prodId,
            @RequestBody ProductStatusDTO statusDTO)
    {
        String status = statusDTO.status().trim();
        Product updatedProduct = productsService.updateProductStatus(prodId, status);
        return (updatedProduct != null) ? ResponseEntity.ok(updatedProduct) : ResponseEntity.notFound().build();
    }

}
