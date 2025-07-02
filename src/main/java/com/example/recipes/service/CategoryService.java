package com.example.recipes.service;

import com.example.recipes.dto.CategoryResponse;
import com.example.recipes.entity.Category;
import com.example.recipes.exception.DuplicateResourceException;
import com.example.recipes.exception.ResourceNotFoundException;
import com.example.recipes.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service לקטגוריות
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    /**
     * יצירת קטגוריה חדשה
     */
    public CategoryResponse createCategory(String name, String description, String color) {
        // בדיקה אם שם הקטגוריה כבר קיים
        if (categoryRepository.existsByName(name)) {
            throw new DuplicateResourceException("קטגוריה", "שם", name);
        }
        
        Category category = Category.builder()
                .name(name)
                .description(description)
                .color(color)
                .build();
        
        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryResponse(savedCategory);
    }
    
    /**
     * קבלת קטגוריה לפי ID
     */
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("קטגוריה", "ID", categoryId));
        
        return convertToCategoryResponse(category);
    }
    
    /**
     * קבלת כל הקטגוריות
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAllByOrderByNameAsc().stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * חיפוש קטגוריות לפי שם
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> searchCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * קבלת הקטגוריות הפופולריות ביותר
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getPopularCategories() {
        return categoryRepository.findCategoriesOrderByRecipeCount().stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * עדכון קטגוריה
     */
    public CategoryResponse updateCategory(Long categoryId, String name, String description, String color) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("קטגוריה", "ID", categoryId));
        
        // בדיקה אם השם החדש כבר קיים (למעט הקטגוריה הנוכחית)
        categoryRepository.findByName(name).ifPresent(existingCategory -> {
            if (!existingCategory.getId().equals(categoryId)) {
                throw new DuplicateResourceException("קטגוריה", "שם", name);
            }
        });
        
        category.setName(name);
        category.setDescription(description);
        category.setColor(color);
        
        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryResponse(savedCategory);
    }
    
    /**
     * מחיקת קטגוריה
     */
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("קטגוריה", "ID", categoryId));
        
        categoryRepository.delete(category);
    }
    
    /**
     * המרה ל-CategoryResponse
     */
    private CategoryResponse convertToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .color(category.getColor())
                .createdAt(category.getCreatedAt())
                .recipesCount((long) category.getRecipes().size())
                .build();
    }
}
