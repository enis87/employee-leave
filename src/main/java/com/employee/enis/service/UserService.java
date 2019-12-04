package com.employee.enis.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.employee.enis.entity.RegistrationUser;
import com.employee.enis.entity.User;

public interface UserService extends UserDetailsService {

	public User findByUserName(String userName);

	public void save(RegistrationUser registrationUser);
	
	
	
	
}
