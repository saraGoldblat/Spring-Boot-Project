package com.example.recipes.controller;

import com.example.recipes.dto.*;
import com.example.recipes.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller למשתמשים
 * Base URL: /api/users
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // TODO: Configure proper CORS
public class UserController {
    
    private final UserService userService;
    
    /**
     * הרשמת משתמש חדש
     * POST /api/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(
            @Valid @RequestBody UserRegistrationRequest request) {
        
        UserResponse userResponse = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("המשתמש נרשם בהצלחה", userResponse));
    }
    
    /**
     * התחברות משתמש
     * POST /api/users/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> loginUser(
            @Valid @RequestBody UserLoginRequest request) {
        
        UserResponse userResponse = userService.loginUser(request);
        return ResponseEntity.ok(ApiResponse.success("התחברות בוצעה בהצלחה", userResponse));
    }
    
    /**
     * קבלת פרטי משתמש לפי ID
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse userResponse = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }
    
    /**
     * קבלת כל המשתמשים (עם pagination)
     * GET /api/users?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> users = userService.getAllActiveUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    /**
     * Health check
     * GET /api/users/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("User service is running"));
    }
}
