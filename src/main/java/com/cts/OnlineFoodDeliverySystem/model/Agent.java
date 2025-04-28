package com.cts.OnlineFoodDeliverySystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Table(name = "agent")
@Data
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable=false,name = "agent_id")
    private Long agentId;  // Unique ID for the agent

    @Column(nullable = false, length = 100)
    private String name;  // Name of the agent

    @Column(nullable = false, length = 100, unique = true)
    private String agentemail;  // Email of the agent

    @Column(nullable = false, length = 15, unique = true)
    private String phoneNumber;  // Phone number of the agent

    @Column(nullable = false, length = 255)
    private String address;  // Address of the agent

    @Min(0)
    @Max(5)
    @Column(nullable = false)
    private Double rating;  // Rating of the agent (e.g., 4.5)

    // Getters and Setters
    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return agentemail;
    }

    public void setEmail(String Agentemail) {
        this.agentemail = Agentemail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
