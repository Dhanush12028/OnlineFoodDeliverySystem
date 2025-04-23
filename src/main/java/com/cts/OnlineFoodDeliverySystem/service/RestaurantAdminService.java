package com.cts.OnlineFoodDeliverySystem.service;

import java.util.List;
import java.util.Optional;

import com.cts.OnlineFoodDeliverySystem.model.MenuItems;
import com.cts.OnlineFoodDeliverySystem.model.RestaurantAdmin;

public interface RestaurantAdminService {

	void registerAdmin(RestaurantAdmin admin);
    Optional<RestaurantAdmin> findAdminByEmail(String email);
    void createItem(MenuItems mitems);
    List<RestaurantAdmin> allRestaurant();
    //int idFromEmail(String email);
}
