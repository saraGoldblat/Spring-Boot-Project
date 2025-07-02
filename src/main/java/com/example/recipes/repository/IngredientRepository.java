package com.example.recipes.repository;

import com.example.recipes.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository למרכיבים
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    
    /**
     * מחפש מרכיב לפי שם
     */
    Optional<Ingredient> findByName(String name);
    
    /**
     * בודק אם קיים מרכיב עם השם הזה
     */
    boolean existsByName(String name);
    
    /**
     * מחפש מרכיבים לפי שם (חיפוש חלקי)
     */
    List<Ingredient> findByNameContainingIgnoreCase(String name);
    
    /**
     * מחזיר את כל המרכיבים מסודרים לפי שם
     */
    List<Ingredient> findAllByOrderByNameAsc();
    
    /**
     * מחזיר מרכיבים עם המספר הגבוה ביותר של שימושים במתכונים
     */
    @Query("SELECT i FROM Ingredient i LEFT JOIN i.recipeIngredients ri GROUP BY i ORDER BY COUNT(ri) DESC")
    List<Ingredient> findIngredientsOrderByUsageCount();
    
    /**
     * מחפש מרכיבים לפי יחידת מידה ברירת מחדל
     */
    List<Ingredient> findByDefaultUnit(String defaultUnit);
}
