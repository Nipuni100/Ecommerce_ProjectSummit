package com.projectSummit.product_service.Repository;

import com.projectSummit.product_service.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {


    Category findByCategoryName(String categoryName);
}