package com.cts.OnlineFoodDeliverySystem.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cts.OnlineFoodDeliverySystem.model.MenuItems;
import com.cts.OnlineFoodDeliverySystem.repository.MenuItemRepository;

@Service
public class MenuItemsServiceimple implements MenuItemsService {

    @Autowired
    private MenuItemRepository menuItemsRepository;

    @Override
    public List<MenuItems> getMenuItemsByRestaurantId(int restaurantId) {
        return menuItemsRepository.findByRestaurantIdNative(restaurantId);
    }

	@Override
	public MenuItems getMenuItemsByItemId(int id) {
		// TODO Auto-generated method stub
		return menuItemsRepository.findById(id).get();
	}

	@Override
	public void deleteItemById(int id) {
		menuItemsRepository.deleteById(id);
		
	}
}
