package com.cts.OnlineFoodDeliverySystem.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Delivery {

	@Id
    @SequenceGenerator(name = "delivery_seq", sequenceName = "delivery_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "delivery_seq")
    @Column(name = "delivery_id")
    private Long deliveryId;

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;
	
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @ManyToOne
    @JoinColumn(name = "delivery_agent_id", referencedColumnName = "agentId")
    private DeliveryAgent assignedDeliveryAgent;

    private Timestamp estimatedDeliveryTime;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

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

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public DeliveryAgent getAssignedDeliveryAgent() {
        return assignedDeliveryAgent;
    }

    public void setAssignedDeliveryAgent(DeliveryAgent assignedDeliveryAgent) {
        this.assignedDeliveryAgent = assignedDeliveryAgent;
    }

    public Timestamp getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(Timestamp estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }
}
