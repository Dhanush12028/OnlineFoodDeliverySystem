package com.cts.OnlineFoodDeliverySystem.service;

import java.util.List;

import com.cts.OnlineFoodDeliverySystem.model.MenuItems;

public interface MenuItemsService {

	List<MenuItems> getMenuItemsByRestaurantId(int restaurantId) ;
	
	MenuItems getMenuItemsByItemId(int id);
	
	void deleteItemById(int id);
}
