package com.cts.OnlineFoodDeliverySystem.controller;
 
import com.cts.OnlineFoodDeliverySystem.model.Delivery;
import com.cts.OnlineFoodDeliverySystem.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.Optional;
 
@RestController
@RequestMapping("/delivery")
public class DeliveryController {
 
    @Autowired
    private DeliveryService deliveryService;
 
    // Get all deliveries
    @GetMapping
    public List<Delivery> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }
 
    // Get delivery by ID
    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable Long id) {
        Optional<Delivery> delivery = deliveryService.getDeliveryById(id);
        return delivery.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }
 
    // Get deliveries by orderId
    @GetMapping("/order/{orderId}")
    public List<Delivery> getDeliveriesByOrderId(@PathVariable Long orderId) {
        return deliveryService.getDeliveriesByOrderId(orderId);
    }
 
    // Get deliveries by status
    @GetMapping("/status/{status}")
    public List<Delivery> getDeliveriesByStatus(@PathVariable String status) {
        return deliveryService.getDeliveriesByStatus(status);
    }
 
    // Get deliveries by agentId
    @GetMapping("/agent/{agentId}")
    public List<Delivery> getDeliveriesByAgentId(@PathVariable Long agentId) {
        return deliveryService.getDeliveriesByAgentId(agentId);
    }
 
    // Add a new delivery
    @PostMapping
    public ResponseEntity<Delivery> addDelivery(@RequestBody Delivery delivery) {
        Delivery savedDelivery = deliveryService.saveDelivery(delivery);
        return ResponseEntity.ok(savedDelivery);
    }
 
    // Delete a delivery by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
        return ResponseEntity.noContent().build();
    }
}