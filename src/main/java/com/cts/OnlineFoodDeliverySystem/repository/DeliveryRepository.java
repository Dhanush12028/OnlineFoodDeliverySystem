package com.cts.OnlineFoodDeliverySystem.repository;

import com.cts.OnlineFoodDeliverySystem.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    @Query("SELECT d FROM Delivery d JOIN FETCH d.order WHERE d.order.orderId = :orderId")
    Optional<Delivery> findByOrder_OrderId(@Param("orderId") Long orderId);
}
