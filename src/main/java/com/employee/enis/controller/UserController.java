package com.employee.enis.controller;


import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.employee.enis.dao.UserDaoImpl;
import com.employee.enis.entity.Leaves;
import com.employee.enis.entity.User;

@Controller
@RequestMapping("/employees")
public class UserController {

	LeavesController controller = new LeavesController();

	@Autowired
	private UserDaoImpl userdao;

	public UserController(UserDaoImpl theEmployeeService) {
		userdao = theEmployeeService;
	}

	// add mapping for "/list"

	@GetMapping("/list")
	public String listEmployees(Model theModel) {

		// get employees from db
		List<User> theEmployees = userdao.findAll();
		
		// add to the spring model
		theModel.addAttribute("employees", theEmployees);		
		
		showNotification(theModel);				
		
		return "/employees/list-employees";
	}
	
	

	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {

		// create model attribute to bind form data
		User theEmployee = new User();

		theModel.addAttribute("employee", theEmployee);

		return "/employees/employee-form";
	}

	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("employeeId") long theId, Model theModel) {

		// get the employee from the service
		User theEmployee = userdao.findById(theId);

		// set employee as a model attribute to pre-populate the form
		theModel.addAttribute("employee", theEmployee);

		// send over to our form
		return "/employees/employee-form";
	}

	@PostMapping("/save")
	public String saveEmployee(@ModelAttribute("employee") User theEmployee) {

		// save the employee
		userdao.save(theEmployee);

		// use a redirect to prevent duplicate submissions
		return "redirect:/employees/list";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("employeeId") long theId) {

		// delete the employee
		userdao.deleteById(theId);

		// redirect to /employees/list
		return "redirect:/employees/list";

	}

	@GetMapping("/search")
	public String delete(@RequestParam("employeeName") String theName, Model theModel) {

		// delete the employee
		List<User> theEmployees = userdao.searchBy(theName);

		// add to the spring model
		theModel.addAttribute("employees", theEmployees);

		// send to /employees/list
		return "/employees/list-employees";

	}

	@Autowired
	private EntityManager entityManager;

	public void showNotification(Model theModel) {
		
/************* Get Notification ******************/
		
		int notify = 0;
		Session currentSession = entityManager.unwrap(Session.class).getSessionFactory().openSession();
		
		// retrieve/read from database
		Query<Leaves> query = currentSession.createQuery("from Leaves", Leaves.class);
		

		List<Leaves> theLeaves = null;
		try {
			theLeaves = query.getResultList();			
			// model.put("leaves", theLeaves);
			if(theLeaves.size() == 0 || theLeaves.size() == -1 ) {
				notify = 0;
				theModel.addAttribute("notification", notify);
				}
		} catch (Exception e) {
			theLeaves = null;
			if (theLeaves == null) {
				notify = 0;
				theModel.addAttribute("notification", notify);
			}
		}

		for (int i = 0; i < theLeaves.size(); i++) {
			theLeaves.get(i).getDescription();
			if(theLeaves.get(i).getDescription()!=null || theLeaves.get(i).getDescription() !=" ") {
				notify++;
				theModel.addAttribute("notification", notify);
				System.out.print(notify);
			}
		}
		
		
		
		/******************************/	

	}

}
