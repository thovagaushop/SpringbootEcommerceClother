package com.project.ecommerce.controllers;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.dto.CreateOrderRequestDTO;
import com.project.ecommerce.dto.OrderDTO;
import com.project.ecommerce.services.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(
        @RequestParam(name = "email", required = true) String email 
    ) {
        List<OrderDTO> orderDTOs = orderService.getOrdersByUser(email);
        return ResponseEntity.ok(orderDTOs);
    }

    @PostMapping("")
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderRequestDTO createOrderRequestDTO) {
        OrderDTO orderDTO = orderService.createOrder(createOrderRequestDTO.getEmail(), createOrderRequestDTO.getPaymentMethod());
        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(
        @PathVariable("id") Long orderId,
        @RequestParam(name = "email", required = true) 
        String email
    ) {
        OrderDTO orderDTO = orderService.getOrder(email, orderId);
        return ResponseEntity.ok(orderDTO);
    }
}
