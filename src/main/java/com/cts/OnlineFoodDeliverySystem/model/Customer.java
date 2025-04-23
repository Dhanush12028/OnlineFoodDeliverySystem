package com.cts.OnlineFoodDeliverySystem.model;
import org.springframework.stereotype.Component;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
@Component
@Entity
@Table(name = "customer")
@NoArgsConstructor
public class Customer {
	@Id
	@Column(name="customerid",length=10)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int customerid;
	
	@Column(name="cname",length=50,nullable = false)
	private String name;
	
	@Column(name="email",length=50,unique = true, nullable = false)
	private String email;
	
	@Column(name = "password", length = 100, nullable = false)
	private String password;
	
	@Column(name="phone",length=10)
	private int phone;
	
	@Column(name="address",length=255)
	private String address;

	public int getCustomerid() {
		return customerid;
	}

	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public Customer() {
		
	}
	public Customer(String name, String email, String password, int phone, String address) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.address = address;
	}
	
}
