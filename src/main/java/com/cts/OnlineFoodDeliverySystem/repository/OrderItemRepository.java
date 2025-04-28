package com.cts.OnlineFoodDeliverySystem.repository;

import com.cts.OnlineFoodDeliverySystem.model.Order;
import com.cts.OnlineFoodDeliverySystem.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
}