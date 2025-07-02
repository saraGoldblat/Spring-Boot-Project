package com.example.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO לתגובת מרכיב
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientResponse {
    
    private Long id;
    private String name;
    private String description;
    private String defaultUnit;
    private LocalDateTime createdAt;
    
    // סטטיסטיקות
    private Long usageCount;
}
