package com.example.recipes.controller;

import com.example.recipes.dto.ApiResponse;
import com.example.recipes.dto.CategoryRequest;
import com.example.recipes.dto.CategoryResponse;
import com.example.recipes.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller לקטגוריות
 * Base URL: /api/categories
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {
    
    private final CategoryService categoryService;
    
    /**
     * יצירת קטגוריה חדשה
     * POST /api/categories
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        
        CategoryResponse categoryResponse = categoryService.createCategory(
                request.getName(), request.getDescription(), request.getColor());
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("הקטגוריה נוצרה בהצלחה", categoryResponse));
    }
    
    /**
     * קבלת קטגוריה לפי ID
     * GET /api/categories/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        CategoryResponse categoryResponse = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(categoryResponse));
    }
    
    /**
     * קבלת כל הקטגוריות
     * GET /api/categories
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }
    
    /**
     * חיפוש קטגוריות לפי שם
     * GET /api/categories/search?name=
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> searchCategories(
            @RequestParam String name) {
        
        List<CategoryResponse> categories = categoryService.searchCategoriesByName(name);
        return ResponseEntity.ok(ApiResponse.success(categories));
    }
    
    /**
     * קבלת הקטגוריות הפופולריות ביותר
     * GET /api/categories/popular
     */
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getPopularCategories() {
        List<CategoryResponse> categories = categoryService.getPopularCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }
    
    /**
     * עדכון קטגוריה
     * PUT /api/categories/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        
        CategoryResponse categoryResponse = categoryService.updateCategory(
                id, request.getName(), request.getDescription(), request.getColor());
        
        return ResponseEntity.ok(ApiResponse.success("הקטגוריה עודכנה בהצלחה", categoryResponse));
    }
    
    /**
     * מחיקת קטגוריה
     * DELETE /api/categories/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("הקטגוריה נמחקה בהצלחה", null));
    }
    
    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("Category service is running"));
    }
}
