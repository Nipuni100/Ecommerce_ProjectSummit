package com.projectSummit.product_service.Repository;

import com.projectSummit.product_service.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {
    Category findByCategoryName(String categoryName);
    boolean existsBycategoryNameIgnoreCase(String categoryName);
}