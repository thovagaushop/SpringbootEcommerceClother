package com.project.ecommerce.services;

import java.util.List;

import com.project.ecommerce.dto.OrderDTO;
import com.project.ecommerce.entities.EOrderStatus;
import com.project.ecommerce.entities.EPaymentMethod;

public interface IOrderService {
    OrderDTO createOrder(String email, EPaymentMethod paymentMethod);
    OrderDTO getOrder(String email, Long orderId);
    List<OrderDTO> getOrdersByUser(String email);
    OrderDTO updateOrder(String email, Long orderId, EOrderStatus orderStatus);
}
