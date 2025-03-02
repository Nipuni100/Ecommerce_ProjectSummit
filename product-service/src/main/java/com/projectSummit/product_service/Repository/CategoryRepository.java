package com.projectSummit.product_service.Repository;

import com.projectSummit.product_service.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Category c WHERE c.categoryId = :categoryId")
    boolean existsById(@Param("categoryId") int categoryId);
    Category findByCategoryName(String CategoryName);
}