package com.example.recipes.repository;

import com.example.recipes.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository לקטגוריות
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * מחפש קטגוריה לפי שם
     */
    Optional<Category> findByName(String name);
    
    /**
     * בודק אם קיימת קטגוריה עם השם הזה
     */
    boolean existsByName(String name);
    
    /**
     * מחפש קטגוריות לפי שם (חיפוש חלקי)
     */
    List<Category> findByNameContainingIgnoreCase(String name);
    
    /**
     * מחזיר את כל הקטגוריות מסודרות לפי שם
     */
    List<Category> findAllByOrderByNameAsc();
    
    /**
     * מחזיר קטגוריות עם המספר הגבוה ביותר של מתכונים
     */
    @Query("SELECT c FROM Category c LEFT JOIN c.recipes r GROUP BY c ORDER BY COUNT(r) DESC")
    List<Category> findCategoriesOrderByRecipeCount();
}
