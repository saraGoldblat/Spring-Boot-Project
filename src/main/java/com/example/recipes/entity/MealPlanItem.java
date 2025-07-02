package com.example.recipes.entity;

import com.example.recipes.enums.MealType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * ישות פריטי תוכנית ארוחות - קושרת מתכון לתאריך וסוג ארוחה
 */
@Entity
@Table(name = "meal_plan_items", indexes = {
    @Index(name = "idx_meal_plan_item_plan", columnList = "meal_plan_id"),
    @Index(name = "idx_meal_plan_item_recipe", columnList = "recipe_id"),
    @Index(name = "idx_meal_plan_item_date", columnList = "meal_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"mealPlan", "recipe"})
@ToString(exclude = {"mealPlan", "recipe"})
public class MealPlanItem extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_plan_id", nullable = false)
    private MealPlan mealPlan;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
    
    @Column(name = "meal_date", nullable = false)
    private LocalDate mealDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType;
    
    @Column(name = "servings")
    @Builder.Default
    private Integer servings = 1;
}
