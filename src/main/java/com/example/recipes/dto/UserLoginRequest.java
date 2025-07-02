package com.example.recipes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO להתחברות משתמש
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginRequest {
    
    @NotBlank(message = "שם משתמש או אימייל הוא שדה חובה")
    private String usernameOrEmail;
    
    @NotBlank(message = "סיסמה הוא שדה חובה")
    private String password;
}
