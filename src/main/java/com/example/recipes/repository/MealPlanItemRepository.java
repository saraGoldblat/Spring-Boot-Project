package com.example.recipes.repository;

import com.example.recipes.entity.MealPlan;
import com.example.recipes.entity.MealPlanItem;
import com.example.recipes.entity.Recipe;
import com.example.recipes.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository לפריטי תוכנית ארוחות
 */
@Repository
public interface MealPlanItemRepository extends JpaRepository<MealPlanItem, Long> {
    
    /**
     * מחפש את כל הפריטים של תוכנית ארוחות
     */
    List<MealPlanItem> findByMealPlan(MealPlan mealPlan);
    
    /**
     * מחפש פריטים לפי תאריך
     */
    List<MealPlanItem> findByMealDate(LocalDate mealDate);
    
    /**
     * מחפש פריטים לפי סוג ארוחה
     */
    List<MealPlanItem> findByMealType(MealType mealType);
    
    /**
     * מחפש פריטים לפי מתכון
     */
    List<MealPlanItem> findByRecipe(Recipe recipe);
    
    /**
     * מחפש פריטים לפי תוכנית ארוחות ותאריך
     */
    List<MealPlanItem> findByMealPlanAndMealDate(MealPlan mealPlan, LocalDate mealDate);
    
    /**
     * מחפש פריטים לפי תוכנית ארוחות וסוג ארוחה
     */
    List<MealPlanItem> findByMealPlanAndMealType(MealPlan mealPlan, MealType mealType);
    
    /**
     * מחפש פריטים לפי תוכנית ארוחות, תאריך וסוג ארוחה
     */
    List<MealPlanItem> findByMealPlanAndMealDateAndMealType(MealPlan mealPlan, LocalDate mealDate, MealType mealType);
    
    /**
     * מחפש פריטים לפי טווח תאריכים
     */
    @Query("SELECT mpi FROM MealPlanItem mpi WHERE mpi.mealPlan.id = :mealPlanId " +
           "AND mpi.mealDate BETWEEN :startDate AND :endDate ORDER BY mpi.mealDate, mpi.mealType")
    List<MealPlanItem> findByMealPlanAndDateRange(@Param("mealPlanId") Long mealPlanId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);
    
    /**
     * מוחק פריטים לפי תוכנית ארוחות
     */
    void deleteByMealPlan(MealPlan mealPlan);
}
