package com.cts.OnlineFoodDeliverySystem.service;

import java.util.Optional;

import com.cts.OnlineFoodDeliverySystem.model.Customer;

public interface CustomerService {

    void registerCustomer(Customer customer); // Assuming you have this

    Optional<Customer> findCustomerByEmail(String email);

    Customer getCustomerByEmail(String email);

    void updateCustomer(Customer customer);
}