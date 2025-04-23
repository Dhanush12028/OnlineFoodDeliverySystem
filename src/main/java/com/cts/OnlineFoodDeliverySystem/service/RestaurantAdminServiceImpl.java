package com.cts.OnlineFoodDeliverySystem.service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.cts.OnlineFoodDeliverySystem.model.MenuItems;
import com.cts.OnlineFoodDeliverySystem.model.RestaurantAdmin;
import com.cts.OnlineFoodDeliverySystem.repository.MenuItemRepository;
import com.cts.OnlineFoodDeliverySystem.repository.RestaurantAdminRepository;

@Service
public class RestaurantAdminServiceImpl implements RestaurantAdminService{
	@Autowired
	private MenuItemRepository menuItemRepository;
    @Autowired
    private RestaurantAdminRepository restaurantAdminRepository;
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
	@Override
	public void registerAdmin(RestaurantAdmin admin) {
		// TODO Auto-generated method stub
		 admin.setPassword(passwordEncoder.encode(admin.getPassword())); // Encode the password
	     restaurantAdminRepository.save(admin);
		
	}

	@Override
	public Optional<RestaurantAdmin> findAdminByEmail(String email) {
		// TODO Auto-generated method stub
        return restaurantAdminRepository.findByEmail(email);
	}

	@Override
	public void createItem(MenuItems mitems) {
		menuItemRepository.save(mitems);
		
	}

	@Override
	public List<RestaurantAdmin> allRestaurant() {
		return restaurantAdminRepository.findAll();
	}

//	@Override
//	public int idFromEmail(String email) {
//		// TODO Auto-generated method stub
//		return restaurantAdminRepository.;
//	}
	
	


}
