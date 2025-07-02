package com.example.recipes.dto;

import com.example.recipes.enums.DifficultyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO ליצירת/עדכון מתכון
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeRequest {
    
    @NotBlank(message = "כותרת המתכון הוא שדה חובה")
    @Size(max = 200, message = "כותרת המתכון יכולה להכיל עד 200 תווים")
    private String title;
    
    @Size(max = 1000, message = "תיאור המתכון יכול להכיל עד 1000 תווים")
    private String description;
    
    @NotBlank(message = "הוראות הכנה הוא שדה חובה")
    private String instructions;
    
    @Positive(message = "זמן הכנה חייב להיות חיובי")
    private Integer prepTime;
    
    @Positive(message = "זמן בישול חייב להיות חיובי")
    private Integer cookTime;
    
    @Positive(message = "מספר מנות חייב להיות חיובי")
    private Integer servings;
    
    private DifficultyLevel difficultyLevel;
    
    private String imageUrl;
    
    @Builder.Default
    private Boolean isPublic = true;
    
    @NotNull(message = "יש לציין קטגוריות למתכון")
    private List<Long> categoryIds;
    
    @NotNull(message = "יש לציין מרכיבים למתכון")
    private List<RecipeIngredientRequest> ingredients;
}
