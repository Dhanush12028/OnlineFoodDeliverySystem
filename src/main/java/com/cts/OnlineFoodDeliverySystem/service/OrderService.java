package com.cts.OnlineFoodDeliverySystem.service;

import java.util.List;

import com.cts.OnlineFoodDeliverySystem.model.Customer;
import com.cts.OnlineFoodDeliverySystem.model.Order;

public interface OrderService {
    Order placeOrder(Customer customer, String deliveryAddress, String paymentMethod);
    Order getOrderById(Long orderId);
    List<Order> getOrdersByCustomer(Customer customer);
    // Add other methods as needed (e.g., updateOrderStatus)
}