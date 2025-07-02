package com.example.recipes.service;

import com.example.recipes.dto.IngredientRequest;
import com.example.recipes.dto.IngredientResponse;
import com.example.recipes.entity.Ingredient;
import com.example.recipes.exception.DuplicateResourceException;
import com.example.recipes.exception.ResourceNotFoundException;
import com.example.recipes.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service למרכיבים
 */
@Service
@RequiredArgsConstructor
@Transactional
public class IngredientService {
    
    private final IngredientRepository ingredientRepository;
    
    /**
     * יצירת מרכיב חדש
     */
    public IngredientResponse createIngredient(IngredientRequest request) {
        // בדיקה אם שם המרכיב כבר קיים
        if (ingredientRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("מרכיב", "שם", request.getName());
        }
        
        Ingredient ingredient = Ingredient.builder()
                .name(request.getName())
                .description(request.getDescription())
                .defaultUnit(request.getDefaultUnit())
                .build();
        
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return convertToIngredientResponse(savedIngredient);
    }
    
    /**
     * קבלת מרכיב לפי ID
     */
    @Transactional(readOnly = true)
    public IngredientResponse getIngredientById(Long ingredientId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ResourceNotFoundException("מרכיב", "ID", ingredientId));
        
        return convertToIngredientResponse(ingredient);
    }
    
    /**
     * קבלת כל המרכיבים
     */
    @Transactional(readOnly = true)
    public List<IngredientResponse> getAllIngredients() {
        return ingredientRepository.findAllByOrderByNameAsc().stream()
                .map(this::convertToIngredientResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * חיפוש מרכיבים לפי שם
     */
    @Transactional(readOnly = true)
    public List<IngredientResponse> searchIngredientsByName(String name) {
        return ingredientRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToIngredientResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * קבלת המרכיבים הפופולריים ביותר
     */
    @Transactional(readOnly = true)
    public List<IngredientResponse> getPopularIngredients() {
        return ingredientRepository.findIngredientsOrderByUsageCount().stream()
                .map(this::convertToIngredientResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * המרה ל-IngredientResponse
     */
    private IngredientResponse convertToIngredientResponse(Ingredient ingredient) {
        return IngredientResponse.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .description(ingredient.getDescription())
                .defaultUnit(ingredient.getDefaultUnit())
                .createdAt(ingredient.getCreatedAt())
                .usageCount((long) ingredient.getRecipeIngredients().size())
                .build();
    }
}
