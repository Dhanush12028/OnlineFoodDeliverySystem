package com.cts.OnlineFoodDeliverySystem.service;

import com.cts.OnlineFoodDeliverySystem.model.*;
import com.cts.OnlineFoodDeliverySystem.repository.*;

import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    
    public DeliveryService(DeliveryRepository deliveryRepo) {
        this.deliveryRepository = deliveryRepo;
      
    }
    

    @Transactional
    public Delivery findByOrderId(Long orderId) {
        return deliveryRepository.findByOrder_OrderId(orderId)
            .orElseGet(() -> createNewDelivery(orderId));
    }

    private Delivery createNewDelivery(Long orderId) {
        Delivery delivery = new Delivery();
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setEstimatedDeliveryTime(
            Timestamp.valueOf(LocalDateTime.now().plusMinutes(30))
        );
        return deliveryRepository.save(delivery);
    }

    @Transactional
    public void updateDeliveryStatus(Long deliveryId, String status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new RuntimeException("Delivery not found"));
        
        DeliveryStatus newStatus = DeliveryStatus.valueOf(status);
        delivery.setStatus(newStatus);
        updateETABasedOnStatus(delivery, newStatus);
        
        
        
        deliveryRepository.save(delivery);
    }

    private void updateETABasedOnStatus(Delivery delivery, DeliveryStatus status) {
        LocalDateTime now = LocalDateTime.now();
        switch(status) {
            case PREPARING:
                delivery.setEstimatedDeliveryTime(Timestamp.valueOf(now.plusMinutes(20)));
                break;
            case OUT_FOR_DELIVERY:
                delivery.setEstimatedDeliveryTime(Timestamp.valueOf(now.plusMinutes(15)));
                break;
            case DELIVERED:
                delivery.setEstimatedDeliveryTime(Timestamp.valueOf(now));
                break;
        }
    }

   
}