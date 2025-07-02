package com.example.recipes.service;

import com.example.recipes.dto.*;
import com.example.recipes.entity.*;
import com.example.recipes.exception.ResourceNotFoundException;
import com.example.recipes.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service למתכונים
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RecipeService {
    
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final FavoriteRepository favoriteRepository;
    private final RatingRepository ratingRepository;
    
    /**
     * יצירת מתכון חדש
     */
    public RecipeResponse createRecipe(RecipeRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("משתמש", "ID", userId));
        
        // יצירת המתכון
        Recipe recipe = Recipe.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .instructions(request.getInstructions())
                .prepTime(request.getPrepTime())
                .cookTime(request.getCookTime())
                .servings(request.getServings())
                .difficultyLevel(request.getDifficultyLevel())
                .imageUrl(request.getImageUrl())
                .isPublic(request.getIsPublic())
                .user(user)
                .build();
        
        // הוספת קטגוריות
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            Set<Category> categories = request.getCategoryIds().stream()
                    .map(categoryId -> categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new ResourceNotFoundException("קטגוריה", "ID", categoryId)))
                    .collect(Collectors.toSet());
            recipe.setCategories(categories);
        }
        
        Recipe savedRecipe = recipeRepository.save(recipe);
        
        // הוספת מרכיבים
        if (request.getIngredients() != null && !request.getIngredients().isEmpty()) {
            List<RecipeIngredient> recipeIngredients = request.getIngredients().stream()
                    .map(ingredientReq -> {
                        Ingredient ingredient = ingredientRepository.findById(ingredientReq.getIngredientId())
                                .orElseThrow(() -> new ResourceNotFoundException("מרכיב", "ID", ingredientReq.getIngredientId()));
                        
                        return RecipeIngredient.builder()
                                .recipe(savedRecipe)
                                .ingredient(ingredient)
                                .quantity(ingredientReq.getQuantity())
                                .unit(ingredientReq.getUnit())
                                .notes(ingredientReq.getNotes())
                                .build();
                    })
                    .collect(Collectors.toList());
            
            recipeIngredientRepository.saveAll(recipeIngredients);
        }
        
        return convertToRecipeResponse(savedRecipe, null);
    }
    
    /**
     * קבלת מתכון לפי ID
     */
    @Transactional(readOnly = true)
    public RecipeResponse getRecipeById(Long recipeId, Long currentUserId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("מתכון", "ID", recipeId));
        
        return convertToRecipeResponse(recipe, currentUserId);
    }
    
    /**
     * קבלת כל המתכונים הציבוריים
     */
    @Transactional(readOnly = true)
    public Page<RecipeResponse> getAllPublicRecipes(Pageable pageable, Long currentUserId) {
        return recipeRepository.findByIsPublicTrue(pageable)
                .map(recipe -> convertToRecipeResponse(recipe, currentUserId));
    }
    
    /**
     * חיפוש מתכונים
     */
    @Transactional(readOnly = true)
    public Page<RecipeResponse> searchRecipes(String title, String categoryName, 
                                            String ingredientName, String difficulty, 
                                            Integer maxTime, Pageable pageable, 
                                            Long currentUserId) {
        
        var difficultyLevel = difficulty != null ? 
                com.example.recipes.enums.DifficultyLevel.valueOf(difficulty.toUpperCase()) : null;
        
        return recipeRepository.searchRecipes(title, categoryName, ingredientName, 
                difficultyLevel, maxTime, pageable)
                .map(recipe -> convertToRecipeResponse(recipe, currentUserId));
    }
    
    /**
     * קבלת מתכונים של משתמש מסוים
     */
    @Transactional(readOnly = true)
    public Page<RecipeResponse> getUserRecipes(Long userId, Pageable pageable, Long currentUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("משתמש", "ID", userId));
        
        return recipeRepository.findByUser(user, pageable)
                .map(recipe -> convertToRecipeResponse(recipe, currentUserId));
    }
    
    /**
     * עדכון מתכון
     */
    public RecipeResponse updateRecipe(Long recipeId, RecipeRequest request, Long userId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("מתכון", "ID", recipeId));
        
        // בדיקה שהמשתמש הוא הבעלים של המתכון
        if (!recipe.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("אין לך הרשאה לערוך מתכון זה");
        }
        
        // עדכון פרטי המתכון
        recipe.setTitle(request.getTitle());
        recipe.setDescription(request.getDescription());
        recipe.setInstructions(request.getInstructions());
        recipe.setPrepTime(request.getPrepTime());
        recipe.setCookTime(request.getCookTime());
        recipe.setServings(request.getServings());
        recipe.setDifficultyLevel(request.getDifficultyLevel());
        recipe.setImageUrl(request.getImageUrl());
        recipe.setIsPublic(request.getIsPublic());
        
        Recipe savedRecipe = recipeRepository.save(recipe);
        return convertToRecipeResponse(savedRecipe, userId);
    }
    
    /**
     * מחיקת מתכון
     */
    public void deleteRecipe(Long recipeId, Long userId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("מתכון", "ID", recipeId));
        
        // בדיקה שהמשתמש הוא הבעלים של המתכון
        if (!recipe.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("אין לך הרשאה למחוק מתכון זה");
        }
        
        recipeRepository.delete(recipe);
    }
    
    /**
     * המרה ל-RecipeResponse
     */
    private RecipeResponse convertToRecipeResponse(Recipe recipe, Long currentUserId) {
        // המרת משתמש
        UserResponse userResponse = UserResponse.builder()
                .id(recipe.getUser().getId())
                .username(recipe.getUser().getUsername())
                .firstName(recipe.getUser().getFirstName())
                .lastName(recipe.getUser().getLastName())
                .profileImageUrl(recipe.getUser().getProfileImageUrl())
                .build();
        
        // המרת קטגוריות
        List<CategoryResponse> categories = recipe.getCategories().stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .color(category.getColor())
                        .build())
                .collect(Collectors.toList());
        
        // המרת מרכיבים
        List<RecipeIngredientResponse> ingredients = recipe.getRecipeIngredients().stream()
                .map(ri -> RecipeIngredientResponse.builder()
                        .id(ri.getId())
                        .ingredientId(ri.getIngredient().getId())
                        .ingredientName(ri.getIngredient().getName())
                        .quantity(ri.getQuantity())
                        .unit(ri.getUnit())
                        .notes(ri.getNotes())
                        .build())
                .collect(Collectors.toList());
        
        // חישוב סטטיסטיקות
        Double averageRating = ratingRepository.calculateAverageRating(recipe.getId());
        Long ratingsCount = ratingRepository.countRatingsByRecipeId(recipe.getId());
        Long favoritesCount = favoriteRepository.countFavoritesByRecipeId(recipe.getId());
        
        Boolean isFavorite = currentUserId != null ? 
                favoriteRepository.existsByUserAndRecipe(
                        userRepository.findById(currentUserId).orElse(null), recipe) : false;
        
        return RecipeResponse.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .instructions(recipe.getInstructions())
                .prepTime(recipe.getPrepTime())
                .cookTime(recipe.getCookTime())
                .totalTime(recipe.getTotalTime())
                .servings(recipe.getServings())
                .difficultyLevel(recipe.getDifficultyLevel())
                .imageUrl(recipe.getImageUrl())
                .isPublic(recipe.getIsPublic())
                .createdAt(recipe.getCreatedAt())
                .updatedAt(recipe.getUpdatedAt())
                .user(userResponse)
                .categories(categories)
                .ingredients(ingredients)
                .averageRating(averageRating != null ? averageRating : 0.0)
                .ratingsCount(ratingsCount != null ? ratingsCount : 0L)
                .favoritesCount(favoritesCount != null ? favoritesCount : 0L)
                .isFavoriteForCurrentUser(isFavorite)
                .build();
    }
}
