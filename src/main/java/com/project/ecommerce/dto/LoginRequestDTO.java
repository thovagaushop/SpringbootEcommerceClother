package com.project.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    
    @Email(message = "Invalid email")
    @NotEmpty(message = "Email required")
    private String email;

    @NotEmpty(message = "Password required")
    private String password;
}
