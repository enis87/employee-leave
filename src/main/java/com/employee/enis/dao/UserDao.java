package com.employee.enis.dao;

import java.util.List;

import com.employee.enis.entity.User;

public interface UserDao {

    public User findByUserName(String userName);
    
    public void save(User user);
    
    public List<User> findAll();
	
	public User findById(long theId);
	
	public void deleteById(long theId);

	public List<User> searchBy(String theName);
	
	
    
}
