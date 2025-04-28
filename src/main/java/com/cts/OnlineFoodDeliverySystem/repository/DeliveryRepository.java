package com.cts.OnlineFoodDeliverySystem.repository;
 
import com.cts.OnlineFoodDeliverySystem.model.Delivery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
 
@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    // Custom query methods can be added as needed
    // For example, find by orderId
    List<Delivery> findByOrderOrderId(Long order_id);
 
    // You can also create a query method to find deliveries by status or agentId
    List<Delivery> findByStatus(String status);
 
    List<Delivery> findByAgent_AgentId(@Param("agentId")Long agentId);
}