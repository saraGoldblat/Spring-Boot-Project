package com.example.recipes.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO ליצירת/עדכון דירוג
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingRequest {
    
    @NotNull(message = "דירוג הוא שדה חובה")
    @Min(value = 1, message = "דירוג חייב להיות לפחות 1")
    @Max(value = 5, message = "דירוג יכול להיות מקסימום 5")
    private Integer rating;
    
    @Size(max = 1000, message = "ביקורת יכולה להכיל עד 1000 תווים")
    private String review;
}
