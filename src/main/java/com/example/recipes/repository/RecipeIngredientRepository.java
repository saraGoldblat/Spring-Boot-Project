package com.example.recipes.repository;

import com.example.recipes.entity.Recipe;
import com.example.recipes.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository למרכיבי מתכון
 */
@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    
    /**
     * מחפש את כל מרכיבי המתכון
     */
    List<RecipeIngredient> findByRecipe(Recipe recipe);
    
    /**
     * מחפש מרכיבי מתכון לפי ID המתכון
     */
    List<RecipeIngredient> findByRecipeId(Long recipeId);
    
    /**
     * מחפש מתכונים המכילים מרכיב מסוים
     */
    @Query("SELECT ri FROM RecipeIngredient ri WHERE ri.ingredient.id = :ingredientId")
    List<RecipeIngredient> findByIngredientId(@Param("ingredientId") Long ingredientId);
    
    /**
     * מוחק את כל מרכיבי המתכון
     */
    void deleteByRecipe(Recipe recipe);
    
    /**
     * מוחק את כל מרכיבי המתכון לפי ID
     */
    void deleteByRecipeId(Long recipeId);
}
