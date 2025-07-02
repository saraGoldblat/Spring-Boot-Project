package com.example.recipes.entity;

import com.example.recipes.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * ישות המתכון - הישות המרכזית במערכת
 */
@Entity
@Table(name = "recipes", indexes = {
    @Index(name = "idx_recipe_title", columnList = "title"),
    @Index(name = "idx_recipe_user", columnList = "user_id"),
    @Index(name = "idx_recipe_public", columnList = "is_public")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"user", "categories", "recipeIngredients", "ratings", "favorites", "mealPlanItems"})
@ToString(exclude = {"user", "categories", "recipeIngredients", "ratings", "favorites", "mealPlanItems"})
public class Recipe extends BaseEntity {
    
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "instructions", columnDefinition = "TEXT", nullable = false)
    private String instructions;
    
    @Column(name = "prep_time")
    private Integer prepTime; // בדקות
    
    @Column(name = "cook_time")
    private Integer cookTime; // בדקות
    
    @Column(name = "servings")
    private Integer servings;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level")
    private DifficultyLevel difficultyLevel;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "is_public")
    @Builder.Default
    private Boolean isPublic = true;
    
    // יחסים
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "recipe_categories",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Builder.Default
    private Set<Category> categories = new HashSet<>();
    
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();
    
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Rating> ratings = new HashSet<>();
    
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Favorite> favorites = new HashSet<>();
    
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MealPlanItem> mealPlanItems = new HashSet<>();
    
    // מתודות עזר
    public Integer getTotalTime() {
        if (prepTime == null && cookTime == null) return null;
        return (prepTime != null ? prepTime : 0) + (cookTime != null ? cookTime : 0);
    }
    
    public Double getAverageRating() {
        if (ratings.isEmpty()) return 0.0;
        return ratings.stream()
                .mapToInt(Rating::getRating)
                .average()
                .orElse(0.0);
    }
}
