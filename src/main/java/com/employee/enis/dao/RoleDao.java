package com.employee.enis.dao;

import com.employee.enis.entity.Role;

public interface RoleDao {

	public Role findRoleByName(String theRoleName);
	
}
