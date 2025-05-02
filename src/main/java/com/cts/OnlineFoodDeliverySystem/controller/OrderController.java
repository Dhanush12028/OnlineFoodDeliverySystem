package com.cts.OnlineFoodDeliverySystem.controller;

import com.cts.OnlineFoodDeliverySystem.model.Customer;
import com.cts.OnlineFoodDeliverySystem.model.Order;
import com.cts.OnlineFoodDeliverySystem.service.CartService;
import com.cts.OnlineFoodDeliverySystem.service.CustomerService;
import com.cts.OnlineFoodDeliverySystem.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CartService cartService;

    private Customer getCurrentCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            String email = authentication.getName();
            return customerService.findCustomerByEmail(email).orElse(null);
        }
        return null;
    }

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

    @GetMapping("/confirmation/{orderId}")
    public String orderConfirmation(@PathVariable String orderId, Model model) {
        try {
            Long id = Long.parseLong(orderId);
            Order order = orderService.getOrderById(id);
            if (order != null) {
                model.addAttribute("orderId", order.getOrderId());
                return "customer/orderConfirmation";
            } else {
                model.addAttribute("errorMessage", "Order not found.");
                return "error"; // Or a specific error page
            }
        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Invalid order ID.");
            return "error"; // Or a specific error page
        }
    }
}