package com.cts.OnlineFoodDeliverySystem.security;
import java.util.Collections;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.cts.OnlineFoodDeliverySystem.model.Customer;
import com.cts.OnlineFoodDeliverySystem.service.CustomerService;
@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Lazy
	@Autowired
    private CustomerService customerService;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<Customer> customerOptional = customerService.findCustomerByEmail(email);
        Customer customer = customerOptional.orElseThrow(() -> new UsernameNotFoundException("Customer not found with email: " + email));

        return new User(customer.getEmail(), customer.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("CUSTOMER")));
	}

}
