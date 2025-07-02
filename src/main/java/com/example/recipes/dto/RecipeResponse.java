package com.example.recipes.dto;

import com.example.recipes.enums.DifficultyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO לתגובת מתכון
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeResponse {
    
    private Long id;
    private String title;
    private String description;
    private String instructions;
    private Integer prepTime;
    private Integer cookTime;
    private Integer totalTime;
    private Integer servings;
    private DifficultyLevel difficultyLevel;
    private String imageUrl;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // מידע על היוצר
    private UserResponse user;
    
    // קטגוריות
    private List<CategoryResponse> categories;
    
    // מרכיבים
    private List<RecipeIngredientResponse> ingredients;
    
    // סטטיסטיקות
    private Double averageRating;
    private Long ratingsCount;
    private Long favoritesCount;
    private Boolean isFavoriteForCurrentUser;
}
