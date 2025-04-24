package com.cts.OnlineFoodDeliverySystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="MENUITEMS")
public class MenuItems {
	@Id
	@Column(name="ITEMID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int itemId;
	@Column(name="NAME",nullable=false)
	private String name;
	@Column(name="DESCRIPTION",nullable=false)
	private String description;
	@Column(name="PRICE",nullable=false)
	private double price;
	//private int restaurantId;
	@Column(name="IMAGE",nullable=false)
    private String image;
	
//	public menuItems(int itemId, String name, String description, double price, int restaurantId) {
//		super();
//		this.itemId = itemId;
//		this.name = name;
//		this.description = description;
//		this.price = price;
//		this.restaurantId = restaurantId;
//	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="restaurant_id")
	private RestaurantAdmin restaurantlist;
	

	public RestaurantAdmin getRestaurantlist() {
		return restaurantlist;
	}
	
	public void setRestaurantlist(RestaurantAdmin restaurantlist) {
		this.restaurantlist = restaurantlist;
	}
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
//	public int getRestaurantId() {
//		return restaurantId;
//	}
//	public void setRestaurantId(int restaurantId) {
//		this.restaurantId = restaurantId;
//	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
//	@Override
//	public String toString() {
//		return "menuItems [itemId=" + itemId + ", name=" + name + ", description=" + description + ", price=" + price
//				+ ", restaurantId=" + restaurantId + "]";
//	}
//	
	
}
