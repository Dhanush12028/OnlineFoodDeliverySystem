package com.cts.OnlineFoodDeliverySystem.service;

import com.cts.OnlineFoodDeliverySystem.model.CartItem;
import com.cts.OnlineFoodDeliverySystem.model.Customer;
import com.cts.OnlineFoodDeliverySystem.model.MenuItems;

import java.util.List;

public interface CartService {
    void addItemToCart(Customer customer, MenuItems menuItem, int quantity);
    List<CartItem> getCartItems(Customer customer);
    void updateCartItemQuantity(Long cartItemId, int quantity);
    void removeCartItem(Long cartItemId);
    void clearCart(Customer customer);
    double calculateCartTotal(Customer customer);
    CartItem findCartItem(Customer customer, MenuItems menuItem);
}