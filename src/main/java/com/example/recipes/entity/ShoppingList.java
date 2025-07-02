package com.example.recipes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * ישות רשימת קניות
 */
@Entity
@Table(name = "shopping_lists", indexes = {
    @Index(name = "idx_shopping_list_user", columnList = "user_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"user", "shoppingListItems"})
@ToString(exclude = {"user", "shoppingListItems"})
public class ShoppingList extends BaseEntity {
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "is_completed")
    @Builder.Default
    private Boolean isCompleted = false;
    
    // יחסים
    @OneToMany(mappedBy = "shoppingList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ShoppingListItem> shoppingListItems = new HashSet<>();
}
