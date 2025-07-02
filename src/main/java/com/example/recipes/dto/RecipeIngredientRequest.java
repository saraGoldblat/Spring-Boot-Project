package com.example.recipes.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO למרכיב במתכון
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredientRequest {
    
    @NotNull(message = "יש לציין מרכיב")
    private Long ingredientId;
    
    @Positive(message = "כמות חייבת להיות חיובית")
    private BigDecimal quantity;
    
    private String unit;
    
    private String notes;
}
