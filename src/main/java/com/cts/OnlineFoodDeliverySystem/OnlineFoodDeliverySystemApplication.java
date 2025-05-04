package com.cts.OnlineFoodDeliverySystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import com.cts.OnlineFoodDeliverySystem.model.DeliveryAgent;
import com.cts.OnlineFoodDeliverySystem.repository.DeliveryAgentRepository;

@SpringBootApplication
public class OnlineFoodDeliverySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineFoodDeliverySystemApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner initializeDeliveryAgents(DeliveryAgentRepository agentRepo) {
        return args -> {
            // Only seed data if no agents exist
            if (agentRepo.count() == 0) {
                agentRepo.save(createAgent("John Doe", "9876543210", 12.9716, 77.5946));
                agentRepo.save(createAgent("Jane Smith", "9123456789", 12.9352, 77.6245));
                agentRepo.save(createAgent("Tom Brown", "9988776655", 12.9665, 77.5873));
                
                System.out.println("✅ 3 delivery agents initialized");
            }
        };
    }

    private DeliveryAgent createAgent(String name, String contact, double lat, double lng) {
        DeliveryAgent agent = new DeliveryAgent();
        agent.setName(name);
        agent.setContactNumber(contact);
        agent.setAvailable(true);
        agent.setCurrentLatitude(lat);
        agent.setCurrentLongitude(lng);
        agent.setLocationUpdatedAt(java.time.LocalDateTime.now());
        return agent;
    }
}