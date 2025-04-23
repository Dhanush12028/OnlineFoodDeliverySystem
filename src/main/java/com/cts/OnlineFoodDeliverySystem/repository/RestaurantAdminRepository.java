package com.cts.OnlineFoodDeliverySystem.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.OnlineFoodDeliverySystem.model.RestaurantAdmin;
@Repository
public interface RestaurantAdminRepository extends JpaRepository<RestaurantAdmin, Integer>{

    Optional<RestaurantAdmin> findByEmail(String email);

    
}

