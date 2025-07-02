package com.example.recipes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * ישות המרכיב
 */
@Entity
@Table(name = "ingredients", indexes = {
    @Index(name = "idx_ingredient_name", columnList = "name")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"recipeIngredients", "shoppingListItems"})
@ToString(exclude = {"recipeIngredients", "shoppingListItems"})
public class Ingredient extends BaseEntity {
    
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "default_unit", length = 20)
    private String defaultUnit;
    
    // יחסים
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();
    
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ShoppingListItem> shoppingListItems = new HashSet<>();
}
