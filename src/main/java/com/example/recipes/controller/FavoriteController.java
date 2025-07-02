package com.example.recipes.controller;

import com.example.recipes.dto.ApiResponse;
import com.example.recipes.dto.RecipeResponse;
import com.example.recipes.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller למועדפים
 * Base URL: /api/favorites
 */
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FavoriteController {
    
    private final FavoriteService favoriteService;
    
    /**
     * הוספת מתכון למועדפים
     * POST /api/favorites/{recipeId}
     */
    @PostMapping("/{recipeId}")
    public ResponseEntity<ApiResponse<Void>> addToFavorites(
            @PathVariable Long recipeId,
            @RequestHeader("User-Id") Long userId) {
        
        favoriteService.addToFavorites(userId, recipeId);
        return ResponseEntity.ok(ApiResponse.success("המתכון נוסף למועדפים", null));
    }
    
    /**
     * הסרת מתכון מהמועדפים
     * DELETE /api/favorites/{recipeId}
     */
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<ApiResponse<Void>> removeFromFavorites(
            @PathVariable Long recipeId,
            @RequestHeader("User-Id") Long userId) {
        
        favoriteService.removeFromFavorites(userId, recipeId);
        return ResponseEntity.ok(ApiResponse.success("המתכון הוסר מהמועדפים", null));
    }
    
    /**
     * קבלת כל המתכונים המועדפים של המשתמש
     * GET /api/favorites?page=0&size=12
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<RecipeResponse>>> getUserFavorites(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestHeader("User-Id") Long userId) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<RecipeResponse> favorites = favoriteService.getUserFavorites(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(favorites));
    }
    
    /**
     * בדיקה אם מתכון הוא מועדף
     * GET /api/favorites/{recipeId}/check
     */
    @GetMapping("/{recipeId}/check")
    public ResponseEntity<ApiResponse<Boolean>> checkIfFavorite(
            @PathVariable Long recipeId,
            @RequestHeader("User-Id") Long userId) {
        
        boolean isFavorite = favoriteService.isRecipeFavorite(userId, recipeId);
        return ResponseEntity.ok(ApiResponse.success(isFavorite));
    }
    
    /**
     * קבלת מספר המועדפים למתכון
     * GET /api/favorites/count/{recipeId}
     */
    @GetMapping("/count/{recipeId}")
    public ResponseEntity<ApiResponse<Long>> getFavoritesCount(@PathVariable Long recipeId) {
        Long count = favoriteService.getFavoritesCount(recipeId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
    
    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("Favorite service is running"));
    }
}
