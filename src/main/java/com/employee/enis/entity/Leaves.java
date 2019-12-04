package com.employee.enis.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "leaves")
public class Leaves {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name="username")
	private String userName;

	@Size(min = 10, message = "Enter at least 10 Characters...")
	private String description;

	@Column(name="start_date")
	private Date targetDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	private int totdays;
	private int remainigdays;
	
	@Column(name="status")
	public String aprovedrejected;
		
	public Leaves() {
		super();
	}

	public Leaves(String user, String desc, Date targetDate,  Date endDate, String status, boolean isDone) {
		super();
		this.userName = user;
		this.description = desc;
		this.targetDate = targetDate;
		this.endDate = endDate;
		this.aprovedrejected = status;
	}

	public Leaves(String desc, Date targetDate,  Date endDate, boolean isDone) {
		this.description = desc;
		this.targetDate = targetDate;
		this.endDate = endDate;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getTotdays() {
		return totdays;
		//return (int) ((endDate.getTime() - targetDate.getTime()) / (1000 * 60 * 60 * 24));
	}

	public int getRemainigdays() {
			return remainigdays;
	}

	public void setTotdays(int totdays) {
		this.totdays = totdays;
	}

	public void setRemainigdays(int remainigdays) {
		this.remainigdays = remainigdays;
	}

	public String getAprovedrejected() {
		return aprovedrejected;
	}

	public void setAprovedrejected(String aprovedrejected) {
		this.aprovedrejected = aprovedrejected;
	}

	
	
	
}