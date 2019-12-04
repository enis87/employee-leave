package com.employee.enis.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.employee.enis.entity.User;
import com.employee.enis.repository.UserRepository;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private EntityManager entityManager;	
	
	private UserRepository userrepository;
	
	@Autowired
	public UserDaoImpl(UserRepository theUserRepository) {
		userrepository = theUserRepository;
	}

	@Override
	public User findByUserName(String theUserName) {
		// get the current hibernate session
		Session currentSession = entityManager.unwrap(Session.class);

		// now retrieve/read from database using username
		Query<User> theQuery = currentSession.createQuery("from User where userName=:uName", User.class);
		theQuery.setParameter("uName", theUserName);
		User theUser = null;
		try {
			theUser = theQuery.getSingleResult();
		} catch (Exception e) {
			theUser = null;
		}

		return theUser;
	}

//	@Override
//	public void save(User theUser) {
//		// get current hibernate session
//		Session currentSession = entityManager.unwrap(Session.class);
//
//		// create the user 
//		currentSession.saveOrUpdate(theUser);
//	}
	
	@Override
	public void save(User theUser) {
		userrepository.save(theUser);
	}
	
	@Override
	public List<User> searchBy(String theName) {
		
		List<User> results = null;
		
		if (theName != null && (theName.trim().length() > 0)) {
			results = userrepository.findByFirstNameContainsOrLastNameContainsAllIgnoreCase(theName, theName);
		}
		else {
			results = findAll();
		}
		
		return results;
	}

	@Override
	public List<User> findAll() {
		return userrepository.findAllByOrderByLastNameAsc();
	}

	@Override
	public User findById(long theId) {
		Optional<User> result = userrepository.findById(theId);
		
		User theEmployee = null;
		
		if (result.isPresent()) {
			theEmployee = result.get();
		}
		else {
			// we didn't find the employee
			throw new RuntimeException("Did not find employee id - " + theId);
		}
		
		return theEmployee;
	}

	@Override
	public void deleteById(long theId) {
		userrepository.deleteById(theId);
		
	}


}
