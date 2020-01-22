/******************************************************************************
 *  Compilation:  javac -d bin ElasticSearchConfig.java
 *  Execution:    java -cp bin com.bridgelabz.config;
 *  						  
 *  
 *  Purpose:      ElasticSearch configuration class
 *  @author  Shubham Chavan
 *  @version 1.0
 *  @since   11-12-2019
 *
 ******************************************************************************/
package com.bridgelabz.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
public class Label {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int labelId;
	private String label;
	
	@ManyToOne
	@JoinColumn(name = "userId",nullable = false)
	@JsonIgnoreProperties({"firstName","lastName","email","mobileNumber","active","password","creationTimeStamp","updateTimeStamp","profilePic"})
	private User user = new User();
	
	/*
	private int userId;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}*/
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name ="Note_Label", joinColumns = {
			@JoinColumn(name = "labelId",nullable = false)},
			inverseJoinColumns = {
					@JoinColumn(name="noteId",nullable = false)})
	@JsonIgnoreProperties(value = "labels")
	private List<Note> notes = new ArrayList<>();
	
	public List<Note> getNotes() {
		return notes;
	}
	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}
	public int getLabelId() {
		return labelId;
	}
	public String getLabel() {
		return label;
	}
	
	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}
}
