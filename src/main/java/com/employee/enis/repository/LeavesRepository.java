package com.employee.enis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employee.enis.entity.Leaves;


public interface LeavesRepository extends JpaRepository<Leaves, Long>{
	List<Leaves> findByUserName(String user);
	
}
