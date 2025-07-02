package com.example.recipes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO ליצירת/עדכון מרכיב
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientRequest {
    
    @NotBlank(message = "שם המרכיב הוא שדה חובה")
    @Size(max = 100, message = "שם המרכיב יכול להכיל עד 100 תווים")
    private String name;
    
    @Size(max = 500, message = "תיאור המרכיב יכול להכיל עד 500 תווים")
    private String description;
    
    @Size(max = 20, message = "יחידת מידה יכולה להכיל עד 20 תווים")
    private String defaultUnit;
}
