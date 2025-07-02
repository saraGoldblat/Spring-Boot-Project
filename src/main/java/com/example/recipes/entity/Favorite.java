package com.example.recipes.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * ישות המועדפים - מאפשרת למשתמשים לשמור מתכונים מועדפים
 */
@Entity
@Table(name = "favorites",
    uniqueConstraints = @UniqueConstraint(name = "unique_user_recipe_favorite", columnNames = {"user_id", "recipe_id"}),
    indexes = {
        @Index(name = "idx_favorite_user", columnList = "user_id"),
        @Index(name = "idx_favorite_recipe", columnList = "recipe_id")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"user", "recipe"})
@ToString(exclude = {"user", "recipe"})
public class Favorite extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
}
