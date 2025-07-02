package com.example.recipes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * ישות מרכיבי המתכון - קושרת בין מתכון למרכיב עם כמות
 */
@Entity
@Table(name = "recipe_ingredients", indexes = {
    @Index(name = "idx_recipe_ingredient_recipe", columnList = "recipe_id"),
    @Index(name = "idx_recipe_ingredient_ingredient", columnList = "ingredient_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"recipe", "ingredient"})
@ToString(exclude = {"recipe", "ingredient"})
public class RecipeIngredient extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;
    
    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;
    
    @Column(name = "unit", length = 20)
    private String unit;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
