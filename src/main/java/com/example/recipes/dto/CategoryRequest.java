package com.example.recipes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO ליצירת/עדכון קטגוריה
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {
    
    @NotBlank(message = "שם הקטגוריה הוא שדה חובה")
    @Size(max = 100, message = "שם הקטגוריה יכול להכיל עד 100 תווים")
    private String name;
    
    @Size(max = 500, message = "תיאור הקטגוריה יכול להכיל עד 500 תווים")
    private String description;
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "צבע חייב להיות בפורמט HEX (#RRGGBB)")
    private String color;
}
