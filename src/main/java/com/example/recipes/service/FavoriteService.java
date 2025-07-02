package com.example.recipes.service;

import com.example.recipes.dto.RecipeResponse;
import com.example.recipes.entity.Favorite;
import com.example.recipes.entity.Recipe;
import com.example.recipes.entity.User;
import com.example.recipes.exception.DuplicateResourceException;
import com.example.recipes.exception.ResourceNotFoundException;
import com.example.recipes.repository.FavoriteRepository;
import com.example.recipes.repository.RecipeRepository;
import com.example.recipes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service למועדפים
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {
    
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeService recipeService;
    
    /**
     * הוספת מתכון למועדפים
     */
    public void addToFavorites(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("משתמש", "ID", userId));
        
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("מתכון", "ID", recipeId));
        
        // בדיקה אם כבר קיים במועדפים
        if (favoriteRepository.existsByUserAndRecipe(user, recipe)) {
            throw new DuplicateResourceException("המתכון כבר קיים במועדפים");
        }
        
        Favorite favorite = Favorite.builder()
                .user(user)
                .recipe(recipe)
                .build();
        
        favoriteRepository.save(favorite);
    }
    
    /**
     * הסרת מתכון מהמועדפים
     */
    public void removeFromFavorites(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("משתמש", "ID", userId));
        
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("מתכון", "ID", recipeId));
        
        if (!favoriteRepository.existsByUserAndRecipe(user, recipe)) {
            throw new ResourceNotFoundException("המתכון לא נמצא במועדפים");
        }
        
        favoriteRepository.deleteByUserAndRecipe(user, recipe);
    }
    
    /**
     * קבלת כל המתכונים המועדפים של משתמש
     */
    @Transactional(readOnly = true)
    public Page<RecipeResponse> getUserFavorites(Long userId, Pageable pageable) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("משתמש", "ID", userId));
        
        return favoriteRepository.findFavoriteRecipesByUserId(userId, pageable)
                .map(recipe -> recipeService.getRecipeById(recipe.getId(), userId));
    }
    
    /**
     * בדיקה אם מתכון הוא מועדף של משתמש
     */
    @Transactional(readOnly = true)
    public boolean isRecipeFavorite(Long userId, Long recipeId) {
        User user = userRepository.findById(userId).orElse(null);
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);
        
        if (user == null || recipe == null) {
            return false;
        }
        
        return favoriteRepository.existsByUserAndRecipe(user, recipe);
    }
    
    /**
     * קבלת מספר המועדפים למתכון
     */
    @Transactional(readOnly = true)
    public Long getFavoritesCount(Long recipeId) {
        return favoriteRepository.countFavoritesByRecipeId(recipeId);
    }
}
