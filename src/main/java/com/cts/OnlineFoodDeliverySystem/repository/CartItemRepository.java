package com.cts.OnlineFoodDeliverySystem.repository;

import com.cts.OnlineFoodDeliverySystem.model.CartItem;
import com.cts.OnlineFoodDeliverySystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCustomer(Customer customer);
    CartItem findByCustomerAndMenuItem_ItemId(Customer customer, int itemId);
}