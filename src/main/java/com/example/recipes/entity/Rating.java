package com.example.recipes.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * ישות הדירוג - מאפשרת למשתמשים לדרג מתכונים
 */
@Entity
@Table(name = "ratings", 
    uniqueConstraints = @UniqueConstraint(name = "unique_user_recipe", columnNames = {"user_id", "recipe_id"}),
    indexes = {
        @Index(name = "idx_rating_recipe", columnList = "recipe_id"),
        @Index(name = "idx_rating_user", columnList = "user_id")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"user", "recipe"})
@ToString(exclude = {"user", "recipe"})
public class Rating extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
    
    @Column(name = "rating", nullable = false)
    private Integer rating; // 1-5
    
    @Column(name = "review", columnDefinition = "TEXT")
    private String review;
    
    @PrePersist
    @PreUpdate
    private void validateRating() {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("הדירוג חייב להיות בין 1 ל-5");
        }
    }
}
