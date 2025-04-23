package com.cts.OnlineFoodDeliverySystem.service;

import java.util.Optional;

import com.cts.OnlineFoodDeliverySystem.model.Customer;

public interface CustomerService {

	void registerCustomer(Customer customer);
	Optional<Customer> findCustomerByEmail(String email);	
}
