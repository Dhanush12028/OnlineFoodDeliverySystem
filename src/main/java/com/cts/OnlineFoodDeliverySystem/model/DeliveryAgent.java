package com.cts.OnlineFoodDeliverySystem.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DeliveryAgent {
    public Long getId() {
		return agentId;
	}
	public void setId(Long agentId) {
		this.agentId = agentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public Double getCurrentLatitude() {
		return currentLatitude;
	}
	public void setCurrentLatitude(Double currentLatitude) {
		this.currentLatitude = currentLatitude;
	}
	public Double getCurrentLongitude() {
		return currentLongitude;
	}
	public void setCurrentLongitude(Double currentLongitude) {
		this.currentLongitude = currentLongitude;
	}
	public LocalDateTime getLocationUpdatedAt() {
		return locationUpdatedAt;
	}
	public void setLocationUpdatedAt(LocalDateTime locationUpdatedAt) {
		this.locationUpdatedAt = locationUpdatedAt;
	}
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long agentId;
    
    private String name;
    private String contactNumber;
    
    @Column(nullable = false, columnDefinition = "NUMBER(1) DEFAULT 1")
    private boolean available = true;
    
    // For location tracking (optional)
    private Double currentLatitude;
    private Double currentLongitude;
    private LocalDateTime locationUpdatedAt;

    // Constructors, getters, and setters
    // (Lombok @Data will generate these automatically)
}