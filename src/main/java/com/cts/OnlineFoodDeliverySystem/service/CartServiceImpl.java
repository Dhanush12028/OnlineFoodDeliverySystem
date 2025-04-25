package com.cts.OnlineFoodDeliverySystem.service;

import com.cts.OnlineFoodDeliverySystem.model.CartItem;
import com.cts.OnlineFoodDeliverySystem.model.Customer;
import com.cts.OnlineFoodDeliverySystem.model.MenuItems;
import com.cts.OnlineFoodDeliverySystem.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public void addItemToCart(Customer customer, MenuItems menuItem, int quantity) {
        CartItem existingItem = cartItemRepository.findByCustomerAndMenuItem_ItemId(customer, menuItem.getItemId());

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCustomer(customer);
            newItem.setMenuItem(menuItem);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }
    }

    @Override
    public List<CartItem> getCartItems(Customer customer) {
        return cartItemRepository.findByCustomer(customer);
    }

    @Override
    public void updateCartItemQuantity(Long cartItemId, int quantity) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);
        cartItemOptional.ifPresent(cartItem -> {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        });
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void clearCart(Customer customer) {
        List<CartItem> cartItems = cartItemRepository.findByCustomer(customer);
        cartItemRepository.deleteAll(cartItems);
    }

    @Override
    public double calculateCartTotal(Customer customer) {
        List<CartItem> cartItems = cartItemRepository.findByCustomer(customer);
        return cartItems.stream()
                .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
                .sum();
    }

    @Override
    public CartItem findCartItem(Customer customer, MenuItems menuItem) {
        return cartItemRepository.findByCustomerAndMenuItem_ItemId(customer, menuItem.getItemId());
    }
}