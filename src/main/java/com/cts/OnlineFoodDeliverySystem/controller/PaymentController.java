package com.cts.OnlineFoodDeliverySystem.controller;

import com.razorpay.RazorpayException;
import com.cts.OnlineFoodDeliverySystem.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService razorpayService;

    @PostMapping("/create-order")
    public String createOrder(@RequestParam int amount, @RequestParam String currency, Model model) {
        try {
            String orderId = razorpayService.createOrder(amount, currency, "recepient_100");
            model.addAttribute("orderId", orderId);
            return "PaymentSuccess";
        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/payment-success")
    public String paymentSuccess(@RequestParam("orderId") Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "PaymentSuccess";
    }

    @GetMapping("/track-delivery")
    public String trackDelivery(@RequestParam(value = "deliveryId", required = false) Long deliveryId, Model model) {
        if (deliveryId != null) {
            // Fetch delivery status from the database
            // For example:
            // DeliveryStatus status = deliveryService.getDeliveryStatus(deliveryId);
            // model.addAttribute("status", status);
        }
        return "TrackDelivery";
    }
}
