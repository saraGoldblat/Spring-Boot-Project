package com.example.recipes.repository;

import com.example.recipes.entity.Favorite;
import com.example.recipes.entity.Recipe;
import com.example.recipes.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository למועדפים
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    /**
     * מחפש מועדף של משתמש למתכון מסוים
     */
    Optional<Favorite> findByUserAndRecipe(User user, Recipe recipe);
    
    /**
     * מחפש את כל המועדפים של משתמש
     */
    Page<Favorite> findByUser(User user, Pageable pageable);
    
    /**
     * מחפש את כל המועדפים של מתכון
     */
    List<Favorite> findByRecipe(Recipe recipe);
    
    /**
     * בודק אם מתכון הוא מועדף של משתמש
     */
    boolean existsByUserAndRecipe(User user, Recipe recipe);
    
    /**
     * מוחק מועדף
     */
    void deleteByUserAndRecipe(User user, Recipe recipe);
    
    /**
     * ספירת מועדפים למתכון
     */
    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.recipe.id = :recipeId")
    Long countFavoritesByRecipeId(@Param("recipeId") Long recipeId);
    
    /**
     * מחזיר את המתכונים המועדפים של משתמש
     */
    @Query("SELECT f.recipe FROM Favorite f WHERE f.user.id = :userId ORDER BY f.createdAt DESC")
    Page<Recipe> findFavoriteRecipesByUserId(@Param("userId") Long userId, Pageable pageable);
}
