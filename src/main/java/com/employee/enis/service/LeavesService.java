package com.employee.enis.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.enis.entity.Leaves;
import com.employee.enis.repository.LeavesRepository;

@Service
public class LeavesService implements ILeavesService {

	@Autowired
	private LeavesRepository leavesRepository;

	@Override
	public List<Leaves> getLeavesByUser(String user) {
		return leavesRepository.findByUserName(user);
	}

	@Override
	public Optional<Leaves> getLeavesById(long id) {
		return leavesRepository.findById(id);
	}

	@Override
	public void updateLeaves(Leaves daysoff) {
		leavesRepository.save(daysoff);
	}

	@Override
	public void addLeaves(String name, String desc, Date targetDate, Date endDate, String status, boolean isDone) {
		leavesRepository.save(new Leaves(name, desc, targetDate, endDate, status, isDone));
	}

	@Override
	public void deleteLeaves(long id) {
		Optional<Leaves> leave = leavesRepository.findById(id);
		if (leave.isPresent()) {
			leavesRepository.delete(leave.get());
		}
	}

	@Override
	public void saveLeaves(Leaves leave) {
		leavesRepository.save(leave);
	}
	
}