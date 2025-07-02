package com.example.recipes.repository;

import com.example.recipes.entity.Recipe;
import com.example.recipes.entity.User;
import com.example.recipes.enums.DifficultyLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository למתכונים
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    
    /**
     * מחפש מתכונים לפי כותרת (חיפוש חלקי)
     */
    Page<Recipe> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    /**
     * מחפש מתכונים ציבוריים בלבד
     */
    Page<Recipe> findByIsPublicTrue(Pageable pageable);
    
    /**
     * מחפש מתכונים של משתמש מסוים
     */
    Page<Recipe> findByUser(User user, Pageable pageable);
    
    /**
     * מחפש מתכונים לפי רמת קושי
     */
    Page<Recipe> findByDifficultyLevel(DifficultyLevel difficultyLevel, Pageable pageable);
    
    /**
     * מחפש מתכונים לפי זמן הכנה מקסימלי
     */
    Page<Recipe> findByTotalTimeLessThanEqual(Integer maxTime, Pageable pageable);
    
    /**
     * מחפש מתכונים לפי קטגוריה
     */
    @Query("SELECT r FROM Recipe r JOIN r.categories c WHERE c.name = :categoryName AND r.isPublic = true")
    Page<Recipe> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);
    
    /**
     * מחפש מתכונים עם דירוג גבוה
     */
    @Query("SELECT r FROM Recipe r JOIN r.ratings rt GROUP BY r HAVING AVG(rt.rating) >= :minRating")
    Page<Recipe> findByMinimumRating(@Param("minRating") Double minRating, Pageable pageable);
    
    /**
     * חיפוש מתכונים מתקדם
     */
    @Query("SELECT DISTINCT r FROM Recipe r " +
           "LEFT JOIN r.categories c " +
           "LEFT JOIN r.recipeIngredients ri " +
           "LEFT JOIN ri.ingredient i " +
           "WHERE (:title IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
           "AND (:categoryName IS NULL OR c.name = :categoryName) " +
           "AND (:ingredientName IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :ingredientName, '%'))) " +
           "AND (:difficultyLevel IS NULL OR r.difficultyLevel = :difficultyLevel) " +
           "AND (:maxTime IS NULL OR (COALESCE(r.prepTime, 0) + COALESCE(r.cookTime, 0)) <= :maxTime) " +
           "AND r.isPublic = true")
    Page<Recipe> searchRecipes(@Param("title") String title,
                              @Param("categoryName") String categoryName,
                              @Param("ingredientName") String ingredientName,
                              @Param("difficultyLevel") DifficultyLevel difficultyLevel,
                              @Param("maxTime") Integer maxTime,
                              Pageable pageable);
    
    /**
     * מחפש את המתכונים הפופולריים ביותר
     */
    @Query("SELECT r FROM Recipe r LEFT JOIN r.favorites f GROUP BY r ORDER BY COUNT(f) DESC")
    List<Recipe> findMostPopularRecipes(Pageable pageable);
}
