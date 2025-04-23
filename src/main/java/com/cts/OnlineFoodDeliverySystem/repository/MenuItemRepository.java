package com.cts.OnlineFoodDeliverySystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.OnlineFoodDeliverySystem.model.MenuItems;

public interface MenuItemRepository extends JpaRepository<MenuItems,Integer>{

	@Query(value = "SELECT * FROM MENUITEMS WHERE restaurant_id = :restaurantId", nativeQuery = true)
	List<MenuItems> findByRestaurantIdNative(@Param("restaurantId") int restaurantId);

}
