package com.projectSummit.product_service.Controller;

import com.projectSummit.product_service.DTOs.ProductRequestDTO;
import com.projectSummit.product_service.DTOs.ProductResponseDTO;
import com.projectSummit.product_service.Entity.Category;
import com.projectSummit.product_service.Entity.Product;
import com.projectSummit.product_service.Service.ProductsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/api/v1/products")
public class ProductsController {
    private final ProductsService productsService;

    @Autowired
    public ProductsController(ProductsService productsService) {

        this.productsService = productsService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getProducts(
            @RequestParam String type,
            @RequestParam String searchQuery) {
        List<ProductResponseDTO> products = productsService.getProduct(type, searchQuery);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product savedProduct = productsService.addProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @GetMapping("/{prodId}")
    public Product getProductById(@PathVariable int prodId) {

        return productsService.getProductById(prodId);
    }

    @DeleteMapping("/{prodId}")
    public void deleteProduct(@PathVariable int prodId) {

        productsService.removeProduct(prodId);
    }

    @PatchMapping("/{prodId}")
    public ResponseEntity<Product> updateProductStatus(
            @PathVariable int prodId,
            @RequestBody String status) {

        Product updatedProduct = productsService.updateProductStatus(prodId, status);

        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
