package com.cts.OnlineFoodDeliverySystem.service;

import java.util.List;

import com.cts.OnlineFoodDeliverySystem.model.Customer;
import com.cts.OnlineFoodDeliverySystem.model.Order;

public interface OrderService {
    Order placeOrder(Customer customer, String deliveryAddress, String paymentMethod);
    Order getOrderById(Long orderId);
    List<Order> getOrdersByCustomer(Customer customer);
    Order processOrderAfterPayment(Customer customer, String razorpayPaymentId, String razorpayOrderId, String deliveryAddress, String paymentMethod);
    List<Order> getRecentOrdersByCustomer(Customer customer);
}