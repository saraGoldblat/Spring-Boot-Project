package com.example.recipes.service;

import com.example.recipes.dto.RatingRequest;
import com.example.recipes.dto.RatingResponse;
import com.example.recipes.dto.UserResponse;
import com.example.recipes.entity.Rating;
import com.example.recipes.entity.Recipe;
import com.example.recipes.entity.User;
import com.example.recipes.exception.DuplicateResourceException;
import com.example.recipes.exception.ResourceNotFoundException;
import com.example.recipes.repository.RatingRepository;
import com.example.recipes.repository.RecipeRepository;
import com.example.recipes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service לדירוגים
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RatingService {
    
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    
    /**
     * יצירת דירוג חדש
     */
    public RatingResponse createRating(Long userId, Long recipeId, RatingRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("משתמש", "ID", userId));
        
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("מתכון", "ID", recipeId));
        
        // בדיקה אם המשתמש כבר דירג את המתכון
        if (ratingRepository.findByUserAndRecipe(user, recipe).isPresent()) {
            throw new DuplicateResourceException("המשתמש כבר דירג את המתכון הזה");
        }
        
        Rating rating = Rating.builder()
                .user(user)
                .recipe(recipe)
                .rating(request.getRating())
                .review(request.getReview())
                .build();
        
        Rating savedRating = ratingRepository.save(rating);
        return convertToRatingResponse(savedRating);
    }
    
    /**
     * עדכון דירוג קיים
     */
    public RatingResponse updateRating(Long userId, Long recipeId, RatingRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("משתמש", "ID", userId));
        
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("מתכון", "ID", recipeId));
        
        Rating rating = ratingRepository.findByUserAndRecipe(user, recipe)
                .orElseThrow(() -> new ResourceNotFoundException("דירוג לא נמצא"));
        
        rating.setRating(request.getRating());
        rating.setReview(request.getReview());
        
        Rating savedRating = ratingRepository.save(rating);
        return convertToRatingResponse(savedRating);
    }
    
    /**
     * מחיקת דירוג
     */
    public void deleteRating(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("משתמש", "ID", userId));
        
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("מתכון", "ID", recipeId));
        
        Rating rating = ratingRepository.findByUserAndRecipe(user, recipe)
                .orElseThrow(() -> new ResourceNotFoundException("דירוג לא נמצא"));
        
        ratingRepository.delete(rating);
    }
    
    /**
     * קבלת דירוג של משתמש למתכון
     */
    @Transactional(readOnly = true)
    public RatingResponse getUserRatingForRecipe(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("משתמש", "ID", userId));
        
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("מתכון", "ID", recipeId));
        
        Rating rating = ratingRepository.findByUserAndRecipe(user, recipe)
                .orElseThrow(() -> new ResourceNotFoundException("דירוג לא נמצא"));
        
        return convertToRatingResponse(rating);
    }
    
    /**
     * קבלת כל הדירוגים למתכון
     */
    @Transactional(readOnly = true)
    public Page<RatingResponse> getRecipeRatings(Long recipeId, Pageable pageable) {
        // וידוא שהמתכון קיים
        recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("מתכון", "ID", recipeId));
        
        return ratingRepository.findRatingsWithReviewsByRecipeId(recipeId, pageable)
                .map(this::convertToRatingResponse);
    }
    
    /**
     * קבלת כל הדירוגים של משתמש
     */
    @Transactional(readOnly = true)
    public Page<RatingResponse> getUserRatings(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("משתמש", "ID", userId));
        
        return ratingRepository.findByUser(user, pageable)
                .map(this::convertToRatingResponse);
    }
    
    /**
     * קבלת דירוג ממוצע למתכון
     */
    @Transactional(readOnly = true)
    public Double getAverageRating(Long recipeId) {
        Double average = ratingRepository.calculateAverageRating(recipeId);
        return average != null ? average : 0.0;
    }
    
    /**
     * קבלת מספר הדירוגים למתכון
     */
    @Transactional(readOnly = true)
    public Long getRatingsCount(Long recipeId) {
        return ratingRepository.countRatingsByRecipeId(recipeId);
    }
    
    /**
     * המרה ל-RatingResponse
     */
    private RatingResponse convertToRatingResponse(Rating rating) {
        UserResponse userResponse = UserResponse.builder()
                .id(rating.getUser().getId())
                .username(rating.getUser().getUsername())
                .firstName(rating.getUser().getFirstName())
                .lastName(rating.getUser().getLastName())
                .profileImageUrl(rating.getUser().getProfileImageUrl())
                .build();
        
        return RatingResponse.builder()
                .id(rating.getId())
                .rating(rating.getRating())
                .review(rating.getReview())
                .createdAt(rating.getCreatedAt())
                .updatedAt(rating.getUpdatedAt())
                .user(userResponse)
                .recipeId(rating.getRecipe().getId())
                .recipeTitle(rating.getRecipe().getTitle())
                .build();
    }
}
