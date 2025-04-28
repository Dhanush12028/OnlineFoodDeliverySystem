package com.cts.OnlineFoodDeliverySystem.controller;

import com.cts.OnlineFoodDeliverySystem.model.Agent;
import com.cts.OnlineFoodDeliverySystem.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    // Get all agents
    @GetMapping
    public List<Agent> getAllAgents() {
        return agentService.getAllAgents();
    }

    // Get agent by ID
    @GetMapping("/{id}")
    public ResponseEntity<Agent> getAgentById(@PathVariable Long id) {
        Optional<Agent> agent = agentService.getAgentById(id);
        return agent.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Add a new agent
    @PostMapping
    public ResponseEntity<Agent> addAgent(@RequestBody Agent agent) {
        Agent savedAgent = agentService.saveAgent(agent);
        return ResponseEntity.ok(savedAgent);
    }

    // Delete an agent by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
        agentService.deleteAgent(id);
        return ResponseEntity.noContent().build();
    }
}
