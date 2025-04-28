package com.cts.OnlineFoodDeliverySystem.repository;

import com.cts.OnlineFoodDeliverySystem.model.Agent;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    // You can add custom query methods here if needed.
    // For example:
    List<Agent> findByName(String name);
    List<Agent> findByRatingGreaterThan(Double rating);
    Optional<Agent> findByAgentemail(String agentemail);
}