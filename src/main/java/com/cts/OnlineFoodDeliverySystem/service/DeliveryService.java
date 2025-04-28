package com.cts.OnlineFoodDeliverySystem.service;
 
import com.cts.OnlineFoodDeliverySystem.model.Delivery;
import com.cts.OnlineFoodDeliverySystem.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.Optional;
 
@Service
public class DeliveryService {
 
    @Autowired
    private DeliveryRepository deliveryRepository;
 
    // Save a new delivery
    public Delivery saveDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }
 
    // Get all deliveries
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }
 
    // Get delivery by ID
    public Optional<Delivery> getDeliveryById(Long id) {
        return deliveryRepository.findById(id);
    }
 
    // Get deliveries by orderId
    public List<Delivery> getDeliveriesByOrderId(Long orderId) {
        return deliveryRepository.findByOrderOrderId(orderId);
    }
 
    // Get deliveries by status
    public List<Delivery> getDeliveriesByStatus(String status) {
        return deliveryRepository.findByStatus(status);
    }
 
    // Get deliveries by agentId
    public List<Delivery> getDeliveriesByAgentId(Long agentId) {
        return deliveryRepository.findByAgent_AgentId(agentId);
    }
 
    // Delete a delivery by ID
    public void deleteDelivery(Long id) {
        deliveryRepository.deleteById(id);
    }
}