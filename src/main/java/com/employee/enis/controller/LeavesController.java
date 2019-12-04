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
@RequestMapping("/leave")
public class LeavesController {
	
	private int days;
		
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

	@GetMapping("/list-leaves")
	public String showLeaves(ModelMap model) {
		
		  String name = getLoggedInUserName(model); 
		  model.put("leaves",  leavesService.getLeavesByUser(name));
		  
		return "list-leaves";
	}

	// Get logged-in User
	private String getLoggedInUserName(ModelMap model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}

		return principal.toString();
	}
	
	
	@GetMapping("/add-leave")
	public String showAddLeavesPage(ModelMap model) {
		model.addAttribute("leaves", new Leaves());
		return "leaves";
	}

	@GetMapping("/delete-leave")
	public String deleteLeaves(@RequestParam long id) {
		leavesService.deleteLeaves(id);
		// service.deleteLeaves(id);
		return "redirect:/leave/list-leaves";
	}

	@GetMapping("/update-leave")
	public String showUpdatLeavesPage(@RequestParam long id, ModelMap model) {
		Leaves leave = leavesService.getLeavesById(id).get();
		model.put("leaves", leave);
		return "leaves";
	}

	@PostMapping("/update-leave")
	public String updateLeaves(ModelMap model, @Valid Leaves leave, BindingResult result) {
	
		if (result.hasErrors()) {
			return "leaves";
		}

		leave.setUserName(getLoggedInUserName(model));
		leavesService.updateLeaves(leave);		
		return "redirect:/leave/list-leaves";
	}

	@PostMapping("/add-leave")
	public String addLeaves(ModelMap model, @Valid Leaves leave, BindingResult result) {

		if (result.hasErrors()) {
			return "leaves";
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
		return "redirect:/leave/list-leaves";
	}

	public int daysSumBetween(Date d1, Date d2) {
		return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}
}
