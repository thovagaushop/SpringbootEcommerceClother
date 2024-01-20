package com.project.ecommerce.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotEmpty(message = "Firstname is required")
    @Size(max = 20, message = "First name must be less than 20 characters long")
    private String firstname;

    @NotEmpty(message = "Lastname is required")
    @Size(max = 20, message = "First name must be less than 20 characters long")
    private String lastname;

    @NotEmpty(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone Number must be exactly 10 digits long")
	@Pattern(regexp = "^\\d{10}$", message = "Phone Number must contain only Numbers")
    private String phoneNumber;

    @Email(message = "Invalid email")
    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

    private Set<String> role;
}
