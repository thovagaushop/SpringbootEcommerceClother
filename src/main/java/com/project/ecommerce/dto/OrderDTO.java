package com.project.ecommerce.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.project.ecommerce.entities.EOrderStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
	private String email;
	private List<OrderItemDTO> orderItems = new ArrayList<>();
	private LocalDate orderDate;
	private PaymentDTO payment;
	private Double totalAmount;

    @Enumerated(EnumType.STRING)
	private EOrderStatus orderStatus = EOrderStatus.PENDING;
}
