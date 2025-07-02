package com.example.recipes.enums;

/**
 * סוגי ארוחות
 */
public enum MealType {
    BREAKFAST("ארוחת בוקר"),
    LUNCH("ארוחת צהריים"),
    DINNER("ארוחת ערב"),
    SNACK("חטיף");
    
    private final String hebrewName;
    
    MealType(String hebrewName) {
        this.hebrewName = hebrewName;
    }
    
    public String getHebrewName() {
        return hebrewName;
    }
}
