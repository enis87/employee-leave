package com.employee.enis.service;

import java.util.Date;

import java.util.List;
import java.util.Optional;

import com.employee.enis.entity.Leaves;

public interface ILeavesService {

	List<Leaves> getLeavesByUser(String user);

	Optional<Leaves> getLeavesById(long id);

	void updateLeaves(Leaves leave);

	void addLeaves(String name, String desc, Date targetDate,Date endDate, String status, boolean isDone);

	void deleteLeaves(long id);
	
	void saveLeaves(Leaves leave);

}