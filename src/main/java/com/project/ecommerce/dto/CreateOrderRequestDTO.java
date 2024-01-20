package com.project.ecommerce.dto;

import com.project.ecommerce.entities.EPaymentMethod;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequestDTO {
    @Email
    @NotEmpty
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EPaymentMethod paymentMethod;
}
