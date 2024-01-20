package com.project.ecommerce.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.dto.ExceptionResponseDTO;
import com.project.ecommerce.dto.JwtResponseDTO;
import com.project.ecommerce.dto.LoginRequestDTO;
import com.project.ecommerce.dto.RegisterRequestDTO;
import com.project.ecommerce.dto.SuccessResponseDTO;
import com.project.ecommerce.entities.Cart;
import com.project.ecommerce.entities.ERole;
import com.project.ecommerce.entities.Role;
import com.project.ecommerce.entities.User;
import com.project.ecommerce.entities.UserDetailsImpl;
import com.project.ecommerce.repositories.CartRepository;
import com.project.ecommerce.repositories.RoleRepository;
import com.project.ecommerce.repositories.UserRepository;
import com.project.ecommerce.security.JwtUtil;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtil jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponseDTO(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ExceptionResponseDTO("error", "Email is already in use!"));
        }

        // Create new user's account
        User user = new User(
            registerRequest.getFirstname(),
            registerRequest.getLastname(),
            registerRequest.getPhoneNumber(),
            registerRequest.getEmail(),
            encoder.encode(registerRequest.getPassword())
        );

        Set<String> strRoles = registerRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.size() == 0) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);

        // Save cart
        Cart cart = new Cart();
        user.setCart(cart);
        User savedUser = userRepository.save(user);
        cart.setUser(savedUser);
        cartRepository.save(cart);

        return ResponseEntity.ok(new SuccessResponseDTO("success", "User registered successfully!"));
    }
}
