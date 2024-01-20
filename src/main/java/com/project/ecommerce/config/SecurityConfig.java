package com.project.ecommerce.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.project.ecommerce.security.AuthEntryPointJwt;
import com.project.ecommerce.security.AuthTokenFilter;
import com.project.ecommerce.services.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // @Bean
    // CorsConfigurationSource corsConfigurationSource() {
    //     CorsConfiguration configuration = new CorsConfiguration();
    //     configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
    //     configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    //     configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
    //     configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);
    //     return source;
    // }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // configuration.setAllowedOrigins(Arrays.asList("*")); // Allow all origins or set specific origins
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow all HTTP methods or set specific methods
        configuration.setAllowedHeaders(Arrays.asList("*")); // Allow all headers or set specific headers
        configuration.setAllowCredentials(true); // Allow credentials
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply configuration to all endpoints or set specific endpoints
        return source;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/product").permitAll()
                        // Category
                        .requestMatchers(HttpMethod.POST, "/api/category").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/category/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/category/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/category/**").hasRole("ADMIN")
                        // Special Category
                        .requestMatchers(HttpMethod.POST, "/api/special-category").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/special-category").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/special-category/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/special-category/**").hasRole("ADMIN")
                        // Product
                        .requestMatchers(HttpMethod.POST, "/api/product").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/product/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/product/**").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.GET, "/api/product").hasAnyRole("ADMIN", "USER")
                        // .requestMatchers(HttpMethod.PUT, "/api/product/**").hasRole("ADMIN")
                        // .requestMatchers(HttpMethod.DELETE, "/api/product/**").hasRole("ADMIN")
                        // Cart
                        .requestMatchers(HttpMethod.POST, "/api/cart/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/cart/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/cart").hasAnyRole("ADMIN", "USER")
                        // Order
                        .requestMatchers(HttpMethod.GET, "/api/orders").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/orders").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated());

        
        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}