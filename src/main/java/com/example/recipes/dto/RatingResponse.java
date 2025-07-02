package com.example.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO לתגובת דירוג
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingResponse {
    
    private Long id;
    private Integer rating;
    private String review;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // מידע על המשתמש
    private UserResponse user;
    
    // מידע על המתכון (אופציונלי)
    private Long recipeId;
    private String recipeTitle;
}
