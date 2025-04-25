package com.cts.OnlineFoodDeliverySystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.cts.OnlineFoodDeliverySystem.model.Customer;
import com.cts.OnlineFoodDeliverySystem.service.CartService;
import com.cts.OnlineFoodDeliverySystem.service.CustomerService;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerService customerService;

    private Customer getCurrentCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            String email = authentication.getName();
            return customerService.findCustomerByEmail(email).orElse(null);
        }
        return null;
    }

    @ModelAttribute("cartItemCount")
    public int cartItemCount() {
        Customer customer = getCurrentCustomer();
        if (customer != null) {
            return cartService.getCartItems(customer).size();
        }
        return 0;
    }
}