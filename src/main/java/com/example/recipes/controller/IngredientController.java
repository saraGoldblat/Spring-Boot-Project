package com.example.recipes.controller;

import com.example.recipes.dto.ApiResponse;
import com.example.recipes.dto.IngredientRequest;
import com.example.recipes.dto.IngredientResponse;
import com.example.recipes.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller למרכיבים
 * Base URL: /api/ingredients
 */
@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class IngredientController {
    
    private final IngredientService ingredientService;
    
    /**
     * יצירת מרכיב חדש
     * POST /api/ingredients
     */
    @PostMapping
    public ResponseEntity<ApiResponse<IngredientResponse>> createIngredient(
            @Valid @RequestBody IngredientRequest request) {
        
        IngredientResponse ingredientResponse = ingredientService.createIngredient(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("המרכיב נוצר בהצלחה", ingredientResponse));
    }
    
    /**
     * קבלת מרכיב לפי ID
     * GET /api/ingredients/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IngredientResponse>> getIngredientById(@PathVariable Long id) {
        IngredientResponse ingredientResponse = ingredientService.getIngredientById(id);
        return ResponseEntity.ok(ApiResponse.success(ingredientResponse));
    }
    
    /**
     * קבלת כל המרכיבים
     * GET /api/ingredients
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<IngredientResponse>>> getAllIngredients() {
        List<IngredientResponse> ingredients = ingredientService.getAllIngredients();
        return ResponseEntity.ok(ApiResponse.success(ingredients));
    }
    
    /**
     * חיפוש מרכיבים לפי שם
     * GET /api/ingredients/search?name=
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<IngredientResponse>>> searchIngredients(
            @RequestParam String name) {
        
        List<IngredientResponse> ingredients = ingredientService.searchIngredientsByName(name);
        return ResponseEntity.ok(ApiResponse.success(ingredients));
    }
    
    /**
     * קבלת המרכיבים הפופולריים ביותר
     * GET /api/ingredients/popular
     */
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<IngredientResponse>>> getPopularIngredients() {
        List<IngredientResponse> ingredients = ingredientService.getPopularIngredients();
        return ResponseEntity.ok(ApiResponse.success(ingredients));
    }
    
    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("Ingredient service is running"));
    }
}
