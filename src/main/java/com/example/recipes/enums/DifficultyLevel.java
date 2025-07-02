package com.example.recipes.enums;

/**
 * רמות קושי למתכונים
 */
public enum DifficultyLevel {
    EASY("קל"),
    MEDIUM("בינוני"), 
    HARD("קשה");
    
    private final String hebrewName;
    
    DifficultyLevel(String hebrewName) {
        this.hebrewName = hebrewName;
    }
    
    public String getHebrewName() {
        return hebrewName;
    }
}
