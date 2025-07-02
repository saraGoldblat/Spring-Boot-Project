package com.example.recipes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO להרשמת משתמש חדש
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationRequest {
    
    @NotBlank(message = "שם משתמש הוא שדה חובה")
    @Size(min = 3, max = 50, message = "שם משתמש חייב להיות בין 3 ל-50 תווים")
    private String username;
    
    @NotBlank(message = "אימייל הוא שדה חובה")
    @Email(message = "פורמט האימייל אינו תקין")
    private String email;
    
    @NotBlank(message = "סיסמה הוא שדה חובה")
    @Size(min = 6, message = "הסיסמה חייבת להכיל לפחות 6 תווים")
    private String password;
    
    @Size(max = 50, message = "שם פרטי יכול להכיל עד 50 תווים")
    private String firstName;
    
    @Size(max = 50, message = "שם משפחה יכול להכיל עד 50 תווים")
    private String lastName;
}
