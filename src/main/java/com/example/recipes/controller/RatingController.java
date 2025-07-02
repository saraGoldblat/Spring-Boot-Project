package com.example.recipes.controller;

import com.example.recipes.dto.ApiResponse;
import com.example.recipes.dto.RatingRequest;
import com.example.recipes.dto.RatingResponse;
import com.example.recipes.service.RatingService;
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
 * Controller לדירוגים
 * Base URL: /api/ratings
 */
@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RatingController {
    
    private final RatingService ratingService;
    
    /**
     * יצירת דירוג חדש למתכון
     * POST /api/ratings/recipe/{recipeId}
     */
    @PostMapping("/recipe/{recipeId}")
    public ResponseEntity<ApiResponse<RatingResponse>> createRating(
            @PathVariable Long recipeId,
            @Valid @RequestBody RatingRequest request,
            @RequestHeader("User-Id") Long userId) {
        
        RatingResponse ratingResponse = ratingService.createRating(userId, recipeId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("הדירוג נוסף בהצלחה", ratingResponse));
    }
    
    /**
     * עדכון דירוג קיים
     * PUT /api/ratings/recipe/{recipeId}
     */
    @PutMapping("/recipe/{recipeId}")
    public ResponseEntity<ApiResponse<RatingResponse>> updateRating(
            @PathVariable Long recipeId,
            @Valid @RequestBody RatingRequest request,
            @RequestHeader("User-Id") Long userId) {
        
        RatingResponse ratingResponse = ratingService.updateRating(userId, recipeId, request);
        return ResponseEntity.ok(ApiResponse.success("הדירוג עודכן בהצלחה", ratingResponse));
    }
    
    /**
     * מחיקת דירוג
     * DELETE /api/ratings/recipe/{recipeId}
     */
    @DeleteMapping("/recipe/{recipeId}")
    public ResponseEntity<ApiResponse<Void>> deleteRating(
            @PathVariable Long recipeId,
            @RequestHeader("User-Id") Long userId) {
        
        ratingService.deleteRating(userId, recipeId);
        return ResponseEntity.ok(ApiResponse.success("הדירוג נמחק בהצלחה", null));
    }
    
    /**
     * קבלת דירוג של משתמש למתכון
     * GET /api/ratings/recipe/{recipeId}/my
     */
    @GetMapping("/recipe/{recipeId}/my")
    public ResponseEntity<ApiResponse<RatingResponse>> getMyRatingForRecipe(
            @PathVariable Long recipeId,
            @RequestHeader("User-Id") Long userId) {
        
        RatingResponse ratingResponse = ratingService.getUserRatingForRecipe(userId, recipeId);
        return ResponseEntity.ok(ApiResponse.success(ratingResponse));
    }
    
    /**
     * קבלת כל הדירוגים למתכון
     * GET /api/ratings/recipe/{recipeId}?page=0&size=10
     */
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<ApiResponse<Page<RatingResponse>>> getRecipeRatings(
            @PathVariable Long recipeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<RatingResponse> ratings = ratingService.getRecipeRatings(recipeId, pageable);
        return ResponseEntity.ok(ApiResponse.success(ratings));
    }
    
    /**
     * קבלת כל הדירוגים של משתמש
     * GET /api/ratings/my?page=0&size=10
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<RatingResponse>>> getMyRatings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestHeader("User-Id") Long userId) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<RatingResponse> ratings = ratingService.getUserRatings(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(ratings));
    }
    
    /**
     * קבלת דירוג ממוצע למתכון
     * GET /api/ratings/recipe/{recipeId}/average
     */
    @GetMapping("/recipe/{recipeId}/average")
    public ResponseEntity<ApiResponse<Double>> getAverageRating(@PathVariable Long recipeId) {
        Double averageRating = ratingService.getAverageRating(recipeId);
        return ResponseEntity.ok(ApiResponse.success(averageRating));
    }
    
    /**
     * קבלת מספר הדירוגים למתכון
     * GET /api/ratings/recipe/{recipeId}/count
     */
    @GetMapping("/recipe/{recipeId}/count")
    public ResponseEntity<ApiResponse<Long>> getRatingsCount(@PathVariable Long recipeId) {
        Long count = ratingService.getRatingsCount(recipeId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
    
    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("Rating service is running"));
    }
}
