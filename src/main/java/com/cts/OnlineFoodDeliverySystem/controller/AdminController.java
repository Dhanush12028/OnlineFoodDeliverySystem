package com.cts.OnlineFoodDeliverySystem.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cts.OnlineFoodDeliverySystem.model.MenuItems;
import com.cts.OnlineFoodDeliverySystem.model.RestaurantAdmin;
import com.cts.OnlineFoodDeliverySystem.service.MenuItemsServiceimple;
import com.cts.OnlineFoodDeliverySystem.service.RestaurantAdminService;

@Controller
public class AdminController {

	@Autowired
    private RestaurantAdminService restaurantAdminService;
	@Autowired
	private MenuItemsServiceimple menuItemsService;

    @GetMapping("/admin/register")
    public String showAdminRegistrationForm(Model model) {
        model.addAttribute("admin", new RestaurantAdmin());
        return "admin/register";
    }

    @PostMapping("/admin/register")
    public String registerAdmin(@ModelAttribute("admin") RestaurantAdmin admin, Model model) {
        if (!admin.getEmail().contains("@") || !admin.getEmail().contains(".")) {
            model.addAttribute("error", "Invalid email format");
            return "admin/register";
        }
        restaurantAdminService.registerAdmin(admin);
        model.addAttribute("success", "Restaurant registration successful! You can now log in.");
        return "admin/login";
    } 

    @GetMapping("/admin/login")
    public String showAdminLoginForm() {
        return "admin/login";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
    	
        return "admin/dashboard";
    }
    
    
//    @GetMapping("/admin/dashboard/{emailAdmin}/edit")
//    public String editAdminRestaurant(@PathVariable("emailAdmin") String emailAdmin,Model model) {
//    	Optional<RestaurantAdmin> restAdmin=restaurantAdminService.findAdminByEmail(emailAdmin);
//    	model.addAttribute("details",restAdmin);
//    	return "admin/editRestaurant";
//    }
    
    @GetMapping("/admin/dashboard/{emailAdmin}/edit")
    public String editAdminRestaurant(@PathVariable("emailAdmin") String emailAdmin, Model model) {
    	System.out.println(emailAdmin);
        Optional<RestaurantAdmin> restAdmin = restaurantAdminService.findAdminByEmail(emailAdmin);
        
        if (restAdmin.isPresent()) {
        	List<MenuItems> items=menuItemsService.getMenuItemsByRestaurantId(restAdmin.get().getId());
            model.addAttribute("details", restAdmin.get());
            model.addAttribute("items",items);
            
        } else {
            // Handle the case where the admin is not found
            model.addAttribute("error", "Admin not found");
            return "error";
        }
        return "admin/editRestaurant";
    }

//    @GetMapping("/admin/dashboard/{emailAdmin}/edit/Add")
//    public String AddItems(@PathVariable("emailAdmin") String emailAdmin,Model model) {
//    	MenuItems mitems=new MenuItems();
//    	//mitems.setRestaurantlist(restaurantAdminService.findAdminByEmail(emailAdmin));
//    	model.addAttribute("details",emailAdmin);
//    	model.addAttribute("items",mitems);
//    	return "admin/addItems";
//    }
    
    @GetMapping("/admin/dashboard/{emailAdmin}/edit/Add")
    public String addItems(@PathVariable("emailAdmin") String emailAdmin, Model model) {
        Optional<RestaurantAdmin> restAdmin = restaurantAdminService.findAdminByEmail(emailAdmin);
        if (restAdmin.isPresent()) {
            model.addAttribute("details", restAdmin.get());
        } else {
            model.addAttribute("error", "Admin not found");
            return "error";
        }
        MenuItems mitems = new MenuItems();
        mitems.setRestaurantlist(restAdmin.get());
        model.addAttribute("items", mitems);
        return "admin/addItems";
    }

    
    
    @PostMapping("/admin/dashboard/{emailAdmin}/edit/Add/submit")
    public String createItems(@PathVariable("emailAdmin") String emailAdmin,@ModelAttribute MenuItems mitems) {
    	Optional<RestaurantAdmin> restAdmin = restaurantAdminService.findAdminByEmail(emailAdmin);
    	mitems.setRestaurantlist(restAdmin.get());
    	restaurantAdminService.createItem(mitems);
    	return "redirect:/admin/dashboard/"+emailAdmin+"/edit";
    }
    
    
    @GetMapping("/admin/dashboard/{emailAdmin}/edit/{Id}/Update")
    public String UpdateItem(@PathVariable("emailAdmin") String emailAdmin,@PathVariable("Id") int Id,Model model) {
    	Optional<RestaurantAdmin> restAdmin = restaurantAdminService.findAdminByEmail(emailAdmin);
        if (restAdmin.isPresent()) {
            model.addAttribute("details", restAdmin.get());
        } else {
            model.addAttribute("error", "Admin not found");
            return "error";
        }
    	MenuItems mitem=menuItemsService.getMenuItemsByItemId(Id);
    	model.addAttribute("Item",mitem);
    	return "admin/updateitem";
    }
    
    
    @PostMapping("/admin/dashboard/{emailAdmin}/edit/{Id}/Update/submit")
    	public String updateItems(@PathVariable("emailAdmin") String emailAdmin,@PathVariable("Id") int Id,@ModelAttribute MenuItems mitems) {
    		Optional<RestaurantAdmin> restAdmin = restaurantAdminService.findAdminByEmail(emailAdmin);
        	mitems.setRestaurantlist(restAdmin.get());
        	mitems.setItemId(Id);
        	restaurantAdminService.createItem(mitems);
        	return "redirect:/admin/dashboard/"+emailAdmin+"/edit";
    	}
    
    @GetMapping("/admin/dashboard/{emailAdmin}/edit/{Id}/Delete")
    public String deleteItem(@PathVariable("emailAdmin") String emailAdmin,@PathVariable("Id") int Id) {
    	menuItemsService.deleteItemById(Id);
    	return "redirect:/admin/dashboard/"+emailAdmin+"/edit";
    }
    
    
}
