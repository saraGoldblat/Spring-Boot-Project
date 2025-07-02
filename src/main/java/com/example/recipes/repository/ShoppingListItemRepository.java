package com.example.recipes.repository;

import com.example.recipes.entity.ShoppingList;
import com.example.recipes.entity.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository לפריטי רשימת קניות
 */
@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
    
    /**
     * מחפש את כל הפריטים של רשימת קניות
     */
    List<ShoppingListItem> findByShoppingList(ShoppingList shoppingList);
    
    /**
     * מחפש פריטים שנקנו
     */
    @Query("SELECT sli FROM ShoppingListItem sli WHERE sli.shoppingList.id = :shoppingListId AND sli.isPurchased = true")
    List<ShoppingListItem> findPurchasedItemsByShoppingListId(@Param("shoppingListId") Long shoppingListId);
    
    /**
     * מחפש פריטים שלא נקנו
     */
    @Query("SELECT sli FROM ShoppingListItem sli WHERE sli.shoppingList.id = :shoppingListId AND sli.isPurchased = false")
    List<ShoppingListItem> findUnpurchasedItemsByShoppingListId(@Param("shoppingListId") Long shoppingListId);
    
    /**
     * מחפש פריטים לפי מרכיב
     */
    @Query("SELECT sli FROM ShoppingListItem sli WHERE sli.ingredient.id = :ingredientId")
    List<ShoppingListItem> findByIngredientId(@Param("ingredientId") Long ingredientId);
    
    /**
     * מחפש פריטים מותאמים אישית (ללא מרכיב)
     */
    @Query("SELECT sli FROM ShoppingListItem sli WHERE sli.ingredient IS NULL AND sli.customItem IS NOT NULL")
    List<ShoppingListItem> findCustomItems();
    
    /**
     * ספירת פריטים שנקנו ברשימה
     */
    @Query("SELECT COUNT(sli) FROM ShoppingListItem sli WHERE sli.shoppingList.id = :shoppingListId AND sli.isPurchased = true")
    Long countPurchasedItemsByShoppingListId(@Param("shoppingListId") Long shoppingListId);
    
    /**
     * ספירת כל הפריטים ברשימה
     */
    @Query("SELECT COUNT(sli) FROM ShoppingListItem sli WHERE sli.shoppingList.id = :shoppingListId")
    Long countItemsByShoppingListId(@Param("shoppingListId") Long shoppingListId);
    
    /**
     * מוחק פריטים לפי רשימת קניות
     */
    void deleteByShoppingList(ShoppingList shoppingList);
}
