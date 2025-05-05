package com.cts.OnlineFoodDeliverySystem.controller;

import com.cts.OnlineFoodDeliverySystem.model.*;
import com.cts.OnlineFoodDeliverySystem.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    
    @Autowired private OrderService orderService;
    @Autowired private CustomerService customerService;
    @Autowired private CartService cartService;
    @Autowired private DeliveryService deliveryService;

    // ================== ORDER FLOW ENDPOINTS ================== //
    
    @GetMapping("/checkout")
    public String checkout(Model model) {
        Customer customer = getCurrentCustomer();
        if (customer == null) {
            return "redirect:/customer/login";
        }
        model.addAttribute("cartItems", cartService.getCartItems(customer));
        model.addAttribute("cartTotal", cartService.calculateCartTotal(customer));
        return "customer/checkout";
    }

    @PostMapping("/placeOrder")
    public String placeOrder(@RequestParam("deliveryAddress") String deliveryAddress,
                             @RequestParam("paymentMethod") String paymentMethod,
                             Model model) {
        Customer customer = getCurrentCustomer();
        if (customer == null) {
            return "redirect:/customer/login";
        }

        Order placedOrder = orderService.placeOrder(customer, deliveryAddress, paymentMethod);

        if (placedOrder != null) {
            model.addAttribute("orderId", placedOrder.getOrderId());
            return "customer/orderConfirmation";
        } else {
            model.addAttribute("errorMessage", "There was an issue placing your order.");
            return "customer/checkout";
        }
    }
    
    @PostMapping("/payment-confirmation")
    @ResponseBody
    public ResponseEntity<Map<String, String>> paymentConfirmation(@RequestBody Map<String, String> payload) {
        logger.info("Received payload for /payment-confirmation: {}", payload);

        String razorpayPaymentId = payload.get("razorpay_payment_id");
        String razorpayOrderId = payload.get("razorpay_order_id");
        String deliveryAddress = payload.get("deliveryAddress");
        String paymentMethod = payload.get("paymentMethod");

        logger.info("Extracted paymentId: {}, orderId: {}, deliveryAddress: {}, paymentMethod: {}",
                    razorpayPaymentId, razorpayOrderId, deliveryAddress, paymentMethod);

        Customer customer = getCurrentCustomer();
        if (customer == null) {
            logger.warn("User not authenticated during payment confirmation.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not authenticated"));
        }

        logger.info("Calling orderService.processOrderAfterPayment for customer: {}", customer.getEmail());
        Order placedOrder = orderService.processOrderAfterPayment(customer, razorpayPaymentId, razorpayOrderId, deliveryAddress, paymentMethod);
        logger.info("Returned from orderService.processOrderAfterPayment, result: {}", placedOrder);

        if (placedOrder != null) {
            logger.info("placedOrder after service call: {}", placedOrder);
            logger.info("placedOrder.getOrderId(): {}", placedOrder.getOrderId());
            return ResponseEntity.ok(Map.of("orderId", placedOrder.getOrderId().toString()));
        } else {
            logger.error("Failed to process order after payment for customer: {}", customer.getEmail());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to process order after payment"));
        }
    }

    // ================== ORDER TRACKING ENDPOINTS ================== //
    
    @GetMapping("/confirmation/{orderId}")
    public String showConfirmation(@PathVariable Long orderId,Model model) {
        Order order = orderService.getOrderById(orderId);
    
            
        Delivery delivery = deliveryService.findByOrderId(orderId);
        
        model.addAttribute("order", order);
        model.addAttribute("delivery", delivery);
        
        // Calculate delivery progress
        if (delivery.getEstimatedDeliveryTime() != null) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime eta = delivery.getEstimatedDeliveryTime().toLocalDateTime();
            
            model.addAttribute("etaFormatted", eta.format(DateTimeFormatter.ofPattern("h:mm a")));
            model.addAttribute("remainingMinutes", Duration.between(now, eta).toMinutes());
            model.addAttribute("progressPercent", calculateProgressPercent(now, eta));
        }
        
        return "customer/orderConfirmation";
    }

    @GetMapping("/track/{orderId}")
    public String trackOrder(
        @PathVariable Long orderId,
        Model model
    ) {
        Delivery delivery = deliveryService.findByOrderId(orderId);
        model.addAttribute("delivery", delivery);
        return "order-tracking";
    }

    // ================== STATUS API ENDPOINTS ================== //
    
    @GetMapping("/api/status/{orderId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDeliveryStatus(
        @PathVariable Long orderId
    ) {
        try {
            Delivery delivery = deliveryService.findByOrderId(orderId);
            Map<String, Object> response = new LinkedHashMap<>();
            
            // Basic info
            response.put("status", delivery.getStatus().name());
            response.put("lastUpdated", LocalDateTime.now().toString());
            
            // ETA calculations
            if (delivery.getEstimatedDeliveryTime() != null) {
                LocalDateTime eta = delivery.getEstimatedDeliveryTime().toLocalDateTime();
                response.put("eta", eta.format(DateTimeFormatter.ISO_LOCAL_TIME));
                response.put("remainingMinutes", Duration.between(LocalDateTime.now(), eta).toMinutes());
            }
            
            // Agent info
            if (delivery.getAssignedDeliveryAgent() != null) {
                response.put("agent", Map.of(
                    "name", delivery.getAssignedDeliveryAgent().getName(),
                    "contact", delivery.getAssignedDeliveryAgent().getContactNumber()
                ));
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Status check failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Could not fetch status"));
        }
    }

    // ================== UTILITY METHODS ================== //
    
    private Customer getCurrentCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            String email = authentication.getName();
            return customerService.findCustomerByEmail(email).orElse(null);
        }
        return null;
    }
    
    private int calculateProgressPercent(LocalDateTime start, LocalDateTime end) {
        long totalSeconds = Duration.between(start, end).getSeconds();
        long elapsedSeconds = Duration.between(start, LocalDateTime.now()).getSeconds();
        return (int) ((elapsedSeconds * 100) / Math.max(1, totalSeconds));
    }
}