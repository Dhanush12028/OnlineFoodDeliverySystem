package com.cts.OnlineFoodDeliverySystem.controller;

import com.cts.OnlineFoodDeliverySystem.model.*;
import com.cts.OnlineFoodDeliverySystem.service.*;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {
    private static final String REDIRECT_TO_ORDER = "redirect:/order/confirmation/";
    
    @Autowired private DeliveryService deliveryService;

    @PostMapping("/update-status")
    public String updateStatus(
        @RequestParam Long orderId,
        @RequestParam String newStatus,
        RedirectAttributes redirectAttrs
    ) {
        try {
            Delivery delivery = deliveryService.findByOrderId(orderId);
            deliveryService.updateDeliveryStatus(delivery.getDeliveryId(), newStatus);
            
            redirectAttrs.addFlashAttribute("success", 
                "Status updated to " + newStatus);
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", 
                "Failed to update status: " + e.getMessage());
        }
        
        return REDIRECT_TO_ORDER + orderId;
    }

   

   
}