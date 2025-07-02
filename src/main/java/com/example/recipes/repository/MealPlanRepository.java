package com.example.recipes.repository;

import com.example.recipes.entity.MealPlan;
import com.example.recipes.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository לתוכניות ארוחות
 */
@Repository
public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {
    
    /**
     * מחפש את כל תוכניות הארוחות של משתמש
     */
    Page<MealPlan> findByUser(User user, Pageable pageable);
    
    /**
     * מחפש תוכניות ארוחות לפי שם
     */
    List<MealPlan> findByNameContainingIgnoreCase(String name);
    
    /**
     * מחפש תוכניות ארוחות פעילות (בתחום התאריכים)
     */
    @Query("SELECT mp FROM MealPlan mp WHERE mp.user.id = :userId " +
           "AND (:date BETWEEN mp.startDate AND mp.endDate OR mp.startDate IS NULL OR mp.endDate IS NULL)")
    List<MealPlan> findActiveMealPlansByUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
    
    /**
     * מחפש תוכניות ארוחות לפי טווח תאריכים
     */
    @Query("SELECT mp FROM MealPlan mp WHERE mp.user.id = :userId " +
           "AND ((mp.startDate BETWEEN :startDate AND :endDate) " +
           "OR (mp.endDate BETWEEN :startDate AND :endDate) " +
           "OR (mp.startDate <= :startDate AND mp.endDate >= :endDate))")
    List<MealPlan> findMealPlansByUserAndDateRange(@Param("userId") Long userId, 
                                                   @Param("startDate") LocalDate startDate, 
                                                   @Param("endDate") LocalDate endDate);
}
