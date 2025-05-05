package com.cts.OnlineFoodDeliverySystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.OnlineFoodDeliverySystem.model.Customer;
import com.cts.OnlineFoodDeliverySystem.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerOrderByOrderDateDesc(Customer customer);
    @Query(value = "SELECT * FROM (SELECT o.* FROM FOOD_ORDER o WHERE o.customer_id = :customerId ORDER BY o.order_date DESC) WHERE ROWNUM <= :limit", nativeQuery = true)
    List<Order> findTopRecentOrdersByCustomerId(@Param("customerId") int customerId, @Param("limit") int limit);
}