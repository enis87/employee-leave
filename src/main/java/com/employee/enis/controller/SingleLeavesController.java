package com.employee.enis.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.employee.enis.entity.Leaves;
import com.employee.enis.service.ILeavesService;

@Controller
@RequestMapping("/dayoff")
public class SingleLeavesController {
	
	@Autowired
	private ILeavesService leavesService;

	@Autowired
	private EntityManager entityManager;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// Date - dd/MM/yyyy
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	// Get logged-in User
	private String getLoggedInUserName(ModelMap model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}

		return principal.toString();
	}

	/*
	 * To be managed by Admin for single Employee
	 * 
	 */
	@GetMapping("/list-leave")
	public String showSingleLeavesPage(@RequestParam String username, ModelMap model) {

		Session currentSession = entityManager.unwrap(Session.class).getSessionFactory().openSession();

		// now retrieve/read from database using username
		Query<Leaves> query = currentSession.createQuery("from Leaves where username=:uName", Leaves.class);
		query.setParameter("uName", username);
		List<Leaves> theLeaves = null;
		try {
			theLeaves = query.getResultList();
			model.put("leaves", theLeaves);
		} catch (Exception e) {
			theLeaves = null;
		}
		
		for(int i=0;i<theLeaves.size();i++) {
			
			System.out.println("Status: " + theLeaves.get(i).getAprovedrejected());
		}

		return "single-leaves";
	}
	
	@GetMapping("/notification")
	public String getNotification(ModelMap model) {

		Session currentSession = entityManager.unwrap(Session.class).getSessionFactory().openSession();
		
		Query<Leaves> query = currentSession.createQuery("from Leaves where description <> null", Leaves.class);
		//query.setParameter("uName", username);
		List<Leaves> theLeaves = null;
		try {
			theLeaves = query.getResultList();
			model.put("leaves", theLeaves);
		} catch (Exception e) {
			theLeaves = null;
		}		
				
		return "single-leaves";
	}
	
	
	@GetMapping("/action")
	
	public String showAprovedRejected(@RequestParam String value, @RequestParam Long id, @Valid Leaves leave, ModelMap model) {
		/*
		Session currentSession = entityManager.unwrap(Session.class).getSessionFactory().openSession();
		
			
		String queryString="update Leaves set status =:statusName where id =:id ";
		Query<Leaves> query = currentSession.createQuery(queryString,Leaves.class);
		query.setParameter("statusName", value);
		query.setParameter("id", id);
		int result = query.executeUpdate();
		*/
		

		if(value.equalsIgnoreCase("Aproved")) {
			leave.setAprovedrejected("Aproved");
		}else if(value.equalsIgnoreCase("Rejected")){
			leave.setAprovedrejected("Rejected");
		}		
			
		return "redirect:/dayoff/notification";
	}
	
	/******************************************************************************************/
	
	@GetMapping("/delete-leave")
	public String deleteLeaves(@RequestParam long id,ModelMap model) {
		leavesService.deleteLeaves(id);
		Object username = model.getAttribute("leavesEmployee.userName");
		// service.deleteLeaves(id);
		return "redirect:/dayoff/list-leave?username="+username.toString();
	}
	
	@GetMapping("/update-leave")
	public String showUpdatLeavesPage(@RequestParam long id, ModelMap model) {
		Leaves leave = leavesService.getLeavesById(id).get();
		model.put("leaves", leave);
		return "leavesdayoff";
	}

	@PostMapping("/update-leave")
	public String updateLeaves(ModelMap model, @Valid Leaves leave, BindingResult result) {
	
		if (result.hasErrors()) {
			return "leavesdayoff";
		}

		leave.setUserName(getLoggedInUserName(model));
		leavesService.updateLeaves(leave);		
		return "redirect:/dayoff/list-leave";
	}
	
	
	@PostMapping("/add-leave")
	public String addLeaves(ModelMap model, @Valid Leaves leave, BindingResult result) {
			int days;
		if (result.hasErrors()) {
			return "leavesdayoff";
		}		
			
		Session currentSession = entityManager.unwrap(Session.class).getSessionFactory().openSession();
		
		// retrieve from database using username
		Query<Leaves> query = currentSession.createNativeQuery("SELECT * FROM employee_holidays.leaves where username=:name", Leaves.class);
		query.setParameter("name", getLoggedInUserName(model));
				
		List<Leaves> theLeaves = null;
		
		leave.setUserName(getLoggedInUserName(model));
		leave.setTotdays(daysSumBetween(leave.getTargetDate(), leave.getEndDate()));
		
		try {
			theLeaves = query.list();
			model.put("leaves", theLeaves);
			if(theLeaves.size() == 0 || theLeaves.size() == -1 ) {
				days = 22 - leave.getTotdays();
				leave.setRemainigdays(days);
			}else {
				days = theLeaves.get(theLeaves.size()-1).getRemainigdays();
				leave.setRemainigdays(days-leave.getTotdays());
			}
			
		} catch (Exception e) {
			theLeaves = null;			
			if(theLeaves == null ) {
				days = 22 - leave.getTotdays();
				leave.setRemainigdays(days);
			}			
			e.getStackTrace();
		}
				
		leavesService.saveLeaves(leave);
		return "redirect:/dayoff/list-leave";
	}

	public int daysSumBetween(Date d1, Date d2) {
		return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}
}
