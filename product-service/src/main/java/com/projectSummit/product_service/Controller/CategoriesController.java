package com.projectSummit.product_service.Controller;

import com.projectSummit.product_service.DTOs.CategoryRequestDTO;
import com.projectSummit.product_service.DTOs.CategoryResponseDTO;
import com.projectSummit.product_service.Entity.Category;
import com.projectSummit.product_service.Entity.Product;
import com.projectSummit.product_service.Service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path="/api/v1/categories")
@RequiredArgsConstructor
public class CategoriesController {

    private final ProductsService productsService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> categoryResponseDTOs = productsService.getAllCategories();
        return ResponseEntity.ok(categoryResponseDTOs); // Return the list of DTOs
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        Category savedCategory = productsService.addNewCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @GetMapping("/{categoryId}") // Get category by categoryId
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable int categoryId) {
        CategoryResponseDTO categoryResponseDTO = productsService.getCategoryById(categoryId);
        if (categoryResponseDTO != null) {
            return ResponseEntity.ok(categoryResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable int categoryId) {
        boolean isDeleted = productsService.deleteCategory(categoryId);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Category deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        }
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable int categoryId, @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO updatedCategory = productsService.updateCategory(categoryId, categoryRequestDTO);

        if (updatedCategory != null) {
            return ResponseEntity.ok(updatedCategory);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }






}
