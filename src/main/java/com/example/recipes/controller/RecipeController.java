package com.example.recipes.controller;

import com.example.recipes.dto.*;
import com.example.recipes.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller למתכונים
 * Base URL: /api/recipes
 */
@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // TODO: Configure proper CORS
public class RecipeController {
    
    private final RecipeService recipeService;
    
    /**
     * יצירת מתכון חדש
     * POST /api/recipes
     */
    @PostMapping
    public ResponseEntity<ApiResponse<RecipeResponse>> createRecipe(
            @Valid @RequestBody RecipeRequest request,
            @RequestHeader("User-Id") Long userId) { // TODO: Replace with proper authentication
        
        RecipeResponse recipeResponse = recipeService.createRecipe(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("המתכון נוצר בהצלחה", recipeResponse));
    }
    
    /**
     * קבלת מתכון לפי ID
     * GET /api/recipes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RecipeResponse>> getRecipeById(
            @PathVariable Long id,
            @RequestHeader(value = "User-Id", required = false) Long currentUserId) {
        
        RecipeResponse recipeResponse = recipeService.getRecipeById(id, currentUserId);
        return ResponseEntity.ok(ApiResponse.success(recipeResponse));
    }
    
    /**
     * קבלת כל המתכונים הציבוריים (עם pagination ו-sorting)
     * GET /api/recipes?page=0&size=10&sort=createdAt,desc
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<RecipeResponse>>> getAllRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestHeader(value = "User-Id", required = false) Long currentUserId) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<RecipeResponse> recipes = recipeService.getAllPublicRecipes(pageable, currentUserId);
        return ResponseEntity.ok(ApiResponse.success(recipes));
    }
    
    /**
     * חיפוש מתכונים מתקדם
     * GET /api/recipes/search?title=&category=&ingredient=&difficulty=&maxTime=
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<RecipeResponse>>> searchRecipes(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String ingredient,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) Integer maxTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestHeader(value = "User-Id", required = false) Long currentUserId) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<RecipeResponse> recipes = recipeService.searchRecipes(
                title, category, ingredient, difficulty, maxTime, pageable, currentUserId);
        
        return ResponseEntity.ok(ApiResponse.success(recipes));
    }
    
    /**
     * קבלת מתכונים של משתמש מסוים
     * GET /api/recipes/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<RecipeResponse>>> getUserRecipes(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestHeader(value = "User-Id", required = false) Long currentUserId) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<RecipeResponse> recipes = recipeService.getUserRecipes(userId, pageable, currentUserId);
        return ResponseEntity.ok(ApiResponse.success(recipes));
    }
    
    /**
     * עדכון מתכון
     * PUT /api/recipes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RecipeResponse>> updateRecipe(
            @PathVariable Long id,
            @Valid @RequestBody RecipeRequest request,
            @RequestHeader("User-Id") Long userId) {
        
        RecipeResponse recipeResponse = recipeService.updateRecipe(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success("המתכון עודכן בהצלחה", recipeResponse));
    }
    
    /**
     * מחיקת מתכון
     * DELETE /api/recipes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRecipe(
            @PathVariable Long id,
            @RequestHeader("User-Id") Long userId) {
        
        recipeService.deleteRecipe(id, userId);
        return ResponseEntity.ok(ApiResponse.success("המתכון נמחק בהצלחה", null));
    }
    
    /**
     * Health check
     * GET /api/recipes/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("Recipe service is running"));
    }
}
