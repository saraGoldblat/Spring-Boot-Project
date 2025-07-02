package com.example.recipes.repository;

import com.example.recipes.entity.Rating;
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
 * Repository לדירוגים
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    /**
     * מחפש דירוג של משתמש למתכון מסוים
     */
    Optional<Rating> findByUserAndRecipe(User user, Recipe recipe);
    
    /**
     * מחפש את כל הדירוגים של מתכון
     */
    List<Rating> findByRecipe(Recipe recipe);
    
    /**
     * מחפש את כל הדירוגים של משתמש
     */
    Page<Rating> findByUser(User user, Pageable pageable);
    
    /**
     * מחפש דירוגים לפי ערך דירוג
     */
    List<Rating> findByRating(Integer rating);
    
    /**
     * מחשב דירוג ממוצע למתכון
     */
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.recipe.id = :recipeId")
    Double calculateAverageRating(@Param("recipeId") Long recipeId);
    
    /**
     * ספירת דירוגים למתכון
     */
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.recipe.id = :recipeId")
    Long countRatingsByRecipeId(@Param("recipeId") Long recipeId);
    
    /**
     * מחפש דירוגים עם ביקורות
     */
    @Query("SELECT r FROM Rating r WHERE r.review IS NOT NULL AND r.review != ''")
    Page<Rating> findRatingsWithReviews(Pageable pageable);
    
    /**
     * מחפש דירוגים עם ביקורות למתכון מסוים
     */
    @Query("SELECT r FROM Rating r WHERE r.recipe.id = :recipeId AND r.review IS NOT NULL AND r.review != ''")
    Page<Rating> findRatingsWithReviewsByRecipeId(@Param("recipeId") Long recipeId, Pageable pageable);
}
