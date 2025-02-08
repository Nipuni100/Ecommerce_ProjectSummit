package com.projectSummit.product_service.Repository;

import com.projectSummit.product_service.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {

    boolean existsById(int categoryId);
    Category findByCategoryName(String categoryName);
}