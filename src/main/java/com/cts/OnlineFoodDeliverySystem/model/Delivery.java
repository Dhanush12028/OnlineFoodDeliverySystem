package com.cts.OnlineFoodDeliverySystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "delivery_details")
@Data
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable=false,name="delivery_id")
    private Long deliveryId;  // Unique ID for the delivery

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;  // Reference to the Order entity

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;  // Reference to the Agent entity

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;    // Delivery status (e.g., Pending, Accepted, Preparing, Delivered)

    @Column(nullable = false)
    private String estimatedTimeOfArrival;  // ETA (Estimated Time of Arrival)

    // Getters and Setters
    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public String getEstimatedTimeOfArrival() {
        return estimatedTimeOfArrival;
    }

    public void setEstimatedTimeOfArrival(String estimatedTimeOfArrival) {
        this.estimatedTimeOfArrival = estimatedTimeOfArrival;
    }
}
