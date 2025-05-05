package com.cts.OnlineFoodDeliverySystem.service;

import com.cts.OnlineFoodDeliverySystem.model.CartItem;
import com.cts.OnlineFoodDeliverySystem.model.Customer;
import com.cts.OnlineFoodDeliverySystem.model.Order;
import com.cts.OnlineFoodDeliverySystem.model.OrderItem;
import com.cts.OnlineFoodDeliverySystem.model.Payment;
import com.cts.OnlineFoodDeliverySystem.repository.CartItemRepository;
import com.cts.OnlineFoodDeliverySystem.repository.OrderItemRepository;
import com.cts.OnlineFoodDeliverySystem.repository.OrderRepository;
import com.cts.OnlineFoodDeliverySystem.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private DeliveryService deliveryService;


    @Transactional
    public Order placeOrder(Customer customer, String deliveryAddress, String paymentMethod) {
        logger.info("placeOrder method called for customer: {}", customer.getEmail());
        List<CartItem> cartItems = cartItemRepository.findByCustomer(customer);

        if (cartItems.isEmpty()) {
            logger.warn("Cart is empty for customer: {}", customer.getEmail());
            return null;
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setDeliveryAddress(deliveryAddress);
        order.setPaymentMethod(paymentMethod);
        order.setOrderStatus("PENDING");
        order.setPaymentStatus("PENDING");
        order.setTotalAmount(BigDecimal.valueOf(calculateTotal(cartItems)));

        Order savedOrder = null;
        try {
            logger.info("Attempting to save order: {}", order);
            savedOrder = orderRepository.save(order);
            logger.info("Order saved successfully with ID: {}", savedOrder.getOrderId());
            saveOrderItems(savedOrder, cartItems);
            savedOrder = orderRepository.save(order);
            logger.info("Order saved successfully with ID: {}", savedOrder.getOrderId());

            // Save order items
            saveOrderItems(savedOrder, cartItems);

          

            return savedOrder;

          
        } catch (Exception e) {
            logger.error("Error saving order for customer: {}", customer.getEmail(), e);
            return null;
        }
    }

    @Transactional
    public Order processOrderAfterPayment(Customer customer, String razorpayPaymentId, String razorpayOrderId, String deliveryAddress, String paymentMethod) {
        logger.info("processOrderAfterPayment method called for customer: {}", customer.getEmail());
        List<CartItem> cartItems = cartItemRepository.findByCustomer(customer);

        if (cartItems.isEmpty()) {
            logger.warn("Cart is empty for customer: {}", customer.getEmail());
            return null;
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setDeliveryAddress(deliveryAddress);
        order.setPaymentMethod(paymentMethod);
        order.setOrderStatus("PROCESSING");
        order.setPaymentStatus("SUCCESSFUL");
        order.setTotalAmount(BigDecimal.valueOf(calculateTotal(cartItems)));
        Order savedOrder = null;
        try {
            logger.info("Attempting to save order after payment: {}", order);
            savedOrder = orderRepository.save(order);
            logger.info("Order saved successfully with ID after payment: {}", savedOrder.getOrderId());
            saveOrderItems(savedOrder, cartItems);
            savePayment(savedOrder, paymentMethod, savedOrder.getTotalAmount(), "Successful", razorpayPaymentId, razorpayOrderId);
         
            cartItemRepository.deleteAll(cartItems);
            return savedOrder;
        } catch (Exception e) {
            logger.error("Error saving order after payment for customer: {}", customer.getEmail(), e);
            return null;
        }
    }

    private void saveOrderItems(Order order, List<CartItem> cartItems) {
        logger.info("Saving order items for order ID: {}", order.getOrderId());
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(cartItem.getMenuItem());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(BigDecimal.valueOf(cartItem.getMenuItem().getPrice()));
            orderItem.setSubtotal(BigDecimal.valueOf(cartItem.getMenuItem().getPrice()).multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            try {
                orderItemRepository.save(orderItem);
                logger.info("Saved order item: {}", orderItem);
            } catch (Exception e) {
                logger.error("Error saving order item: {}", orderItem, e);
            }
        }
        logger.info("Finished saving order items for order ID: {}", order.getOrderId());
    }

    private void savePayment(Order order, String paymentMethod, BigDecimal amount, String status, String paymentIdExt, String orderIdExt) {
        logger.info("Saving payment for order ID: {}", order.getOrderId());
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(amount);
        payment.setStatus(status);
        try {
            paymentRepository.save(payment);
            logger.info("Payment saved for order ID: {}", order.getOrderId());
        } catch (Exception e) {
            logger.error("Error saving payment for order ID: {}", order.getOrderId(), e);
        }
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
    @Override
	public List<Order> getRecentOrdersByCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return orderRepository.findTopRecentOrdersByCustomerId(customer.getCustomerid(), 5);
	}
 
}