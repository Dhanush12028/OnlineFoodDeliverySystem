package com.cts.OnlineFoodDeliverySystem.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cts.OnlineFoodDeliverySystem.model.Customer;
import com.cts.OnlineFoodDeliverySystem.model.MenuItems;
import com.cts.OnlineFoodDeliverySystem.model.Order;
import com.cts.OnlineFoodDeliverySystem.model.RestaurantAdmin;
import com.cts.OnlineFoodDeliverySystem.service.CustomerService;
import com.cts.OnlineFoodDeliverySystem.service.MenuItemsService;
import com.cts.OnlineFoodDeliverySystem.service.OrderService;
import com.cts.OnlineFoodDeliverySystem.service.RestaurantAdminService;

@Controller
public class CustomerController {

	 @Autowired
	    private CustomerService customerService;
	 @Autowired
	 	private RestaurantAdminService restaurantAdminService;
	 @Autowired
	 	private MenuItemsService menuItemService;
	 @Autowired
	 	private OrderService orderService;
	 
	    @GetMapping("/customer/register")
	    public String showCustomerRegistrationForm(Model model) {
	        model.addAttribute("customer", new Customer());
	        return "customer/register";
	    }

	    @PostMapping("/customer/register")
	    public String registerCustomer(@ModelAttribute("customer") Customer customer, Model model) {
	        if (!customer.getEmail().contains("@") || !customer.getEmail().contains(".")) {
	            model.addAttribute("error", "Invalid email format");
	            return "customer/register";
	        }
	        customerService.registerCustomer(customer);
	        model.addAttribute("success", "Registration successful! You can now log in.");
	        return "customer/login"; // Or changed here
	    }

	    @GetMapping("/customer/login")
	    public String showCustomerLoginForm() {
	        return "customer/login";
	    }

	    @GetMapping("/customer/dashboard")
	    public String customerDashboard(Model model) {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String currentCustomerEmail = authentication.getName();
	        Customer customer = customerService.getCustomerByEmail(currentCustomerEmail);
	        List<Order> recentOrders = orderService.getRecentOrdersByCustomer(customer);
 
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	        List<String> formattedOrderDates = recentOrders.stream()
	                .map(order -> order.getOrderDate().format(formatter))
	                .toList();
 
	        model.addAttribute("customer", customer);
	        model.addAttribute("recentOrders", recentOrders);
	        model.addAttribute("formattedOrderDates", formattedOrderDates); // Add the formatted dates
 
	        return "customer/dashboard";
	    }
	    
	    @GetMapping("/customer/dashboard/Restaurants")
	    public String DisplayRestaurants(Model model) {
	    	List<RestaurantAdmin> restaurants=restaurantAdminService.allRestaurant();
	    	model.addAttribute("restaurants",restaurants);
	    	return "customer/restaurants";
	    }
	    
	    @GetMapping("/customer/dashboard/Restaurants/{email}/view")
	    public String DisplayItemsInRestaurant(@PathVariable("email") String email,Model model) {
	    	RestaurantAdmin radmin=restaurantAdminService.findAdminByEmail(email).get();
	    	List<MenuItems> mitems=menuItemService.getMenuItemsByRestaurantId(radmin.getId());
	    	model.addAttribute("rest",radmin);
	    	model.addAttribute("items",mitems);
	    	return "customer/displayItems";
	    }
	    @GetMapping("/customer/profile")
	    public String viewProfile(Model model) {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String email = authentication.getName();
	        Customer customer = customerService.getCustomerByEmail(email);
	        model.addAttribute("customer", customer);
	        return "customer/profile";
	    }

	    @GetMapping("/customer/edit-profile")
	    public String editProfileForm(Model model) {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String email = authentication.getName();
	        Customer customer = customerService.getCustomerByEmail(email);
	        model.addAttribute("customer", customer);
	        return "customer/edit-profile";
	    }
	    @PostMapping("/customer/edit-profile")
	    public String saveProfile(@ModelAttribute Customer customer) {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String email = authentication.getName();
	        Customer existingCustomer = customerService.getCustomerByEmail(email);

	        // Ensure the ID of the existing customer is set in the updated customer object
	        customer.setCustomerid(existingCustomer.getCustomerid());

	        // Preserve the existing password
	        customer.setPassword(existingCustomer.getPassword());

	        customerService.updateCustomer(customer);
	        return "redirect:/customer/profile?success=Profile updated successfully!";
	    }
	    
	    
	    @GetMapping("/customer/dashboard/Restaurants/{email}/view/display/{id}")
	    public String DisplayItemsInRestaurant(@PathVariable("email") String email,@PathVariable("id") int id,Model model) {
	    	RestaurantAdmin radmin=restaurantAdminService.findAdminByEmail(email).get();
	    	List<MenuItems> mitems=menuItemService.getMenuItemsByRestaurantId(radmin.getId());
	    	model.addAttribute("rest",radmin);
	    	model.addAttribute("items",id);
	    	return "customer/itemcart";
	    }
	    
}
