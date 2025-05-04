package com.cts.OnlineFoodDeliverySystem.repository;

import com.cts.OnlineFoodDeliverySystem.model.DeliveryAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Long> {
    
    @Query("SELECT da FROM DeliveryAgent da WHERE da.available = true ORDER BY da.id LIMIT 1")
    Optional<DeliveryAgent> findFirstAvailableAgent();
    
    @Modifying
    @Query("UPDATE DeliveryAgent da SET da.available = :available WHERE da.id = :agentId")
    void updateAvailability(@Param("agentId") Long agentId, @Param("available") boolean available);
}