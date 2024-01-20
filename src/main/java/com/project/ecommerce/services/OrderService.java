package com.project.ecommerce.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ecommerce.dto.OrderDTO;
import com.project.ecommerce.dto.OrderItemDTO;
import com.project.ecommerce.entities.Cart;
import com.project.ecommerce.entities.CartItem;
import com.project.ecommerce.entities.EOrderStatus;
import com.project.ecommerce.entities.EPaymentMethod;
import com.project.ecommerce.entities.Order;
import com.project.ecommerce.entities.OrderItem;
import com.project.ecommerce.entities.Payment;
import com.project.ecommerce.entities.Product;
import com.project.ecommerce.exeptions.APIException;
import com.project.ecommerce.exeptions.ResourceNotFoundException;
import com.project.ecommerce.repositories.CartRepository;
import com.project.ecommerce.repositories.OrderItemRepository;
import com.project.ecommerce.repositories.OrderRepository;
import com.project.ecommerce.repositories.PaymentRepository;
import com.project.ecommerce.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class OrderService implements IOrderService {
    @Autowired
    public UserRepository userRepository;

    @Autowired
    public OrderRepository orderRepository;

    @Autowired
    public CartRepository cartRepository;

    @Autowired
    public PaymentRepository paymentReposuitory;

    @Autowired
    public OrderItemRepository orderItemRepository;

    @Autowired
    public UserService userService;

    @Autowired
    public CartService cartService;

    @Autowired
    public ModelMapper modelMapper;

    @Override
    public OrderDTO createOrder(String email, EPaymentMethod paymentMethod) {
        Cart cart = cartRepository.findCartByEmail(email);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", email);
        }

        Order order = new Order();

        order.setEmail(email);
		order.setOrderDate(LocalDate.now());

		order.setTotalAmount(cart.getTotalPrice());
		order.setOrderStatus(EOrderStatus.PENDING);

		Payment payment = new Payment();
		payment.setOrder(order);
		payment.setPaymentMethod(paymentMethod);

		payment = paymentReposuitory.save(payment);

		order.setPayment(payment);

		Order savedOrder = orderRepository.save(order);

		List<CartItem> cartItems = cart.getCartItems();

		if (cartItems.size() == 0) {
			throw new APIException("Cart is empty");
		}

		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();

			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setDiscount(cartItem.getDiscount());
			orderItem.setOrderedProductPrice(cartItem.getProductPrice());
			orderItem.setOrder(savedOrder);

			orderItems.add(orderItem);
		}

		orderItems = orderItemRepository.saveAll(orderItems);

		cart.getCartItems().forEach(item -> {
			int quantity = item.getQuantity();

			Product product = item.getProduct();

			cartService.deleteProductFromCart(cart.getCartId(), item.getProduct().getId());

			product.setQuantity(product.getQuantity() - quantity);
		});

		OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
		
		orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

		return orderDTO;
    }

    @Override
    public OrderDTO getOrder(String email, Long orderId) {
        Order order = orderRepository.findOrderByEmailAndOrderId(email, orderId);

		if (order == null) {
			throw new ResourceNotFoundException("Order", "orderId", orderId);
		}

		return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public List<OrderDTO> getOrdersByUser(String email) {
        List<Order> orders = orderRepository.findAllByEmail(email);

		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		if (orderDTOs.size() == 0) {
			throw new APIException("No orders placed yet by the user with email: " + email);
		}

		return orderDTOs;
    }

    @Override
    public OrderDTO updateOrder(String email, Long orderId, EOrderStatus orderStatus) {
        Order order = orderRepository.findOrderByEmailAndOrderId(email, orderId);

		if (order == null) {
			throw new ResourceNotFoundException("Order", "orderId", orderId);
		}

		order.setOrderStatus(orderStatus);

		return modelMapper.map(order, OrderDTO.class);
    }
    
}
