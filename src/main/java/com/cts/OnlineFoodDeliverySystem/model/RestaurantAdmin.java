package com.cts.OnlineFoodDeliverySystem.model;

import java.awt.MenuItem;
import java.util.List;

import org.springframework.stereotype.Component;

//import com.cts.OnlineFoodDeliverySystem.model.MenuItems;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Component
@Entity
@Table(name = "restaurant")
@NoArgsConstructor
public class RestaurantAdmin {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "restaurant_id", length = 10)
	private int restaurant_id;

	@Column(name = "restaurant_name", length = 100, nullable = false)
	private String restaurantName;

	@Column(name = "admin_name", length = 50, nullable = false)
	private String adminName;

	@Column(name = "email", length = 100, unique = true, nullable = false)
	private String email;

	public int getId() {
		return restaurant_id;
	}

	public void setId(int restaurant_id) {
		this.restaurant_id = restaurant_id;
	}
	

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER,mappedBy="restaurantlist")
	private List<MenuItems> menuitems;
	
	public List<MenuItems> getMenuitems() {
		return menuitems;
	}
	public void setMenuitems(List<MenuItems> menuitems) {
		this.menuitems = menuitems;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "password", length = 100, nullable = false)
	private String password;

	public RestaurantAdmin() {

	}

	public RestaurantAdmin(String restaurantName, String adminName, String email, String password) {
		this.restaurantName = restaurantName;
		this.adminName = adminName;
		this.email = email;
		this.password = password;
	}

}
