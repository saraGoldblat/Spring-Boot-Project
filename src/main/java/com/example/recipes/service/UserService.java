package com.example.recipes.service;

import com.example.recipes.dto.*;
import com.example.recipes.entity.User;
import com.example.recipes.exception.DuplicateResourceException;
import com.example.recipes.exception.ResourceNotFoundException;
import com.example.recipes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service למשתמשים
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    /**
     * רישום משתמש חדש
     */
    public UserResponse registerUser(UserRegistrationRequest request) {
        // בדיקה אם שם המשתמש כבר קיים
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("משתמש", "שם משתמש", request.getUsername());
        }
        
        // בדיקה אם האימייל כבר קיים
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("משתמש", "אימייל", request.getEmail());
        }
        
        // יצירת משתמש חדש
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword()) // TODO: Hash password
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .isActive(true)
                .build();
        
        User savedUser = userRepository.save(user);
        return convertToUserResponse(savedUser);
    }
    
    /**
     * התחברות משתמש
     */
    public UserResponse loginUser(UserLoginRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.getUsernameOrEmail())
                .orElseThrow(() -> new ResourceNotFoundException("משתמש לא נמצא"));
        
        // TODO: Verify password hash
        if (!user.getPassword().equals(request.getPassword())) {
            throw new ResourceNotFoundException("סיסמה שגויה");
        }
        
        if (!user.getIsActive()) {
            throw new ResourceNotFoundException("החשבון אינו פעיל");
        }
        
        return convertToUserResponse(user);
    }
    
    /**
     * קבלת משתמש לפי ID
     */
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("משתמש", "ID", userId));
        
        return convertToUserResponse(user);
    }
    
    /**
     * קבלת כל המשתמשים הפעילים
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllActiveUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::convertToUserResponse);
    }
    
    /**
     * המרה ל-UserResponse
     */
    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profileImageUrl(user.getProfileImageUrl())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .recipesCount((long) user.getRecipes().size())
                .favoritesCount((long) user.getFavorites().size())
                .build();
    }
}
