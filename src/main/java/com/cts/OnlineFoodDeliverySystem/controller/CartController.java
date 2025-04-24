package com.cts.OnlineFoodDeliverySystem.controller;

import com.cts.OnlineFoodDeliverySystem.model.Customer;
import com.cts.OnlineFoodDeliverySystem.model.MenuItems;
import com.cts.OnlineFoodDeliverySystem.service.CartService;
import com.cts.OnlineFoodDeliverySystem.service.CustomerService;
import com.cts.OnlineFoodDeliverySystem.service.MenuItemsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MenuItemsService menuItemsService;

    private Customer getCurrentCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            String email = authentication.getName(); // Assuming the username is the email
            return customerService.findCustomerByEmail(email).orElse(null);
        }
        return null;
    }
    @PostMapping("/add/{itemId}")
    public String addItemToCart(@PathVariable int itemId, @RequestParam(defaultValue = "1") int quantity, HttpSession session) {
        Customer customer = getCurrentCustomer();
        if (customer == null) {
            return "redirect:/customer/login"; // Redirect if not logged in
        }
        MenuItems menuItem = menuItemsService.getMenuItemsByItemId(itemId);
        if (menuItem != null) {
            cartService.addItemToCart(customer, menuItem, quantity);
        }
        return "redirect:/customer/dashboard/Restaurants/" + getCurrentCustomer().getEmail() + "/view"; // Redirect back to the menu
    }

    @GetMapping
    public String viewCart(Model model) {
        Customer customer = getCurrentCustomer();
        if (customer == null) {
            return "redirect:/customer/login";
        }
        model.addAttribute("cartItems", cartService.getCartItems(customer));
        model.addAttribute("cartTotal", cartService.calculateCartTotal(customer));
        return "customer/cart";
    }

    @PostMapping("/update/{cartItemId}")
    public String updateCartItemQuantity(@PathVariable Long cartItemId, @RequestParam int quantity) {
        cartService.updateCartItemQuantity(cartItemId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove/{cartItemId}")
    public String removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        Customer customer = getCurrentCustomer();
        if (customer != null) {
            cartService.clearCart(customer);
        }
        return "redirect:/cart";
    }
}