package com.cts.OnlineFoodDeliverySystem.service;

import com.cts.OnlineFoodDeliverySystem.model.CartItem;
import com.cts.OnlineFoodDeliverySystem.model.Customer;
import com.cts.OnlineFoodDeliverySystem.model.Order;
import com.cts.OnlineFoodDeliverySystem.model.OrderItem;
import com.cts.OnlineFoodDeliverySystem.repository.CartItemRepository;
import com.cts.OnlineFoodDeliverySystem.repository.OrderItemRepository;
import com.cts.OnlineFoodDeliverySystem.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public Order placeOrder(Customer customer, String deliveryAddress, String paymentMethod) {
        List<CartItem> cartItems = cartItemRepository.findByCustomer(customer);

        if (cartItems.isEmpty()) {
            return null; // Or throw an exception: "Cart is empty"
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setDeliveryAddress(deliveryAddress);
        order.setPaymentMethod(paymentMethod);
        order.setOrderStatus("PENDING"); // Default status
        order.setPaymentStatus("PENDING"); // Default status
        order.setTotalAmount(BigDecimal.valueOf(calculateTotal(cartItems)));

        Order savedOrder = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setMenuItem(cartItem.getMenuItem());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(BigDecimal.valueOf(cartItem.getMenuItem().getPrice()));
            orderItem.setSubtotal(BigDecimal.valueOf(cartItem.getMenuItem().getPrice()).multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItemRepository.save(orderItem);
        }

        // Clear the cart after placing the order
        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }

    private double calculateTotal(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
                .sum();
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public List<Order> getOrdersByCustomer(Customer customer) {
        return orderRepository.findByCustomerOrderByOrderDateDesc(customer);
    }
}