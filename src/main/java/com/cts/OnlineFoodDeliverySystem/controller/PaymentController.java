package com.cts.OnlineFoodDeliverySystem.controller;

import com.razorpay.RazorpayException;
import com.cts.OnlineFoodDeliverySystem.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService razorpayService;

    @PostMapping("/create-order")
    public String createOrder(@RequestParam int amount , @RequestParam String currency){

        try {
            return razorpayService.createOrder(amount, currency, "recepient_100");
        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }
    }

}
