package com.example.recipes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * ישות תוכנית ארוחות
 */
@Entity
@Table(name = "meal_plans", indexes = {
    @Index(name = "idx_meal_plan_user", columnList = "user_id"),
    @Index(name = "idx_meal_plan_dates", columnList = "start_date, end_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"user", "mealPlanItems"})
@ToString(exclude = {"user", "mealPlanItems"})
public class MealPlan extends BaseEntity {
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    // יחסים
    @OneToMany(mappedBy = "mealPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MealPlanItem> mealPlanItems = new HashSet<>();
}
