package com.example.recipes.repository;

import com.example.recipes.entity.ShoppingList;
import com.example.recipes.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository לרשימות קניות
 */
@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    
    /**
     * מחפש את כל רשימות הקניות של משתמש
     */
    Page<ShoppingList> findByUser(User user, Pageable pageable);
    
    /**
     * מחפש רשימות קניות לפי שם
     */
    List<ShoppingList> findByNameContainingIgnoreCase(String name);
    
    /**
     * מחפש רשימות קניות פעילות (לא הושלמו)
     */
    @Query("SELECT sl FROM ShoppingList sl WHERE sl.user.id = :userId AND sl.isCompleted = false")
    List<ShoppingList> findActiveShoppingListsByUser(@Param("userId") Long userId);
    
    /**
     * מחפש רשימות קניות שהושלמו
     */
    @Query("SELECT sl FROM ShoppingList sl WHERE sl.user.id = :userId AND sl.isCompleted = true")
    Page<ShoppingList> findCompletedShoppingListsByUser(@Param("userId") Long userId, Pageable pageable);
    
    /**
     * ספירת רשימות קניות פעילות למשתמש
     */
    @Query("SELECT COUNT(sl) FROM ShoppingList sl WHERE sl.user.id = :userId AND sl.isCompleted = false")
    Long countActiveShoppingListsByUser(@Param("userId") Long userId);
}
