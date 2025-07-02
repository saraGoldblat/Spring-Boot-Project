package com.example.recipes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * ישות פריטי רשימת קניות
 */
@Entity
@Table(name = "shopping_list_items", indexes = {
    @Index(name = "idx_shopping_list_item_list", columnList = "shopping_list_id"),
    @Index(name = "idx_shopping_list_item_ingredient", columnList = "ingredient_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"shoppingList", "ingredient"})
@ToString(exclude = {"shoppingList", "ingredient"})
public class ShoppingListItem extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_list_id", nullable = false)
    private ShoppingList shoppingList;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;
    
    @Column(name = "custom_item", length = 100)
    private String customItem; // למקרה של פריט שלא קיים בטבלת המרכיבים
    
    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;
    
    @Column(name = "unit", length = 20)
    private String unit;
    
    @Column(name = "is_purchased")
    @Builder.Default
    private Boolean isPurchased = false;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    // מתודה לקבלת שם הפריט
    public String getItemName() {
        return ingredient != null ? ingredient.getName() : customItem;
    }
}
