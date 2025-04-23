package com.cts.OnlineFoodDeliverySystem.security;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cts.OnlineFoodDeliverySystem.model.RestaurantAdmin;
import com.cts.OnlineFoodDeliverySystem.service.RestaurantAdminService;

@Service("restaurantAdminDetailsService")
public class CustomRestaurantAdminDetailsService implements UserDetailsService{
	@Lazy
    @Autowired
    private RestaurantAdminService restaurantAdminService;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<RestaurantAdmin> adminOptional = restaurantAdminService.findAdminByEmail(email);
        RestaurantAdmin admin = adminOptional.orElseThrow(() -> new UsernameNotFoundException("Restaurant Admin not found with email: " + email));
        return new User(admin.getEmail(), admin.getPassword(), Collections.singletonList(() -> "ADMIN"));
    
	}

}
