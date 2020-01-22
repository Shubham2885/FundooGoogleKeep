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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Notes")
public class Note implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int noteId;
	private String title;
	private String description;
	private String color;
	private String reminder;
	private boolean isTrash = false;
	private boolean isArchive = false;
	private boolean isPin = false;
	
	@CreationTimestamp
	private Date creationTimeStamp;
	@UpdateTimestamp
	private Date updateTimeStamp;

	 
	@ManyToOne
	@JoinColumn(name="id", nullable=false)
	@JsonIgnoreProperties({"firstName","lastName","email","mobileNumber","active","password","creationTimeStamp","updateTimeStamp"})
	private User user = new User();
	
	@Transient
	private List<String> collaborateUser = new ArrayList<>();
	
	public List<String> getCollaborateUser() {
		return collaborateUser;
	}
	public void setCollaborateUser(List<String> collaborateUser) {
		this.collaborateUser = collaborateUser;
	}
	/*
	private int userId;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}*/
/*	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name ="User_Collaborator",joinColumns = {
			@JoinColumn(name ="noteId",nullable = false)},inverseJoinColumns = {
					@JoinColumn(name="id",nullable=false)})
	@JsonIgnoreProperties(value = "listOfNoteId")
	private List<User> listOfUserCollaboratorId = new ArrayList<>();
	
	@JsonIgnoreProperties({"firstName","lastName","email","mobileNumber","active","password","creationTimeStamp","updateTimeStamp"})
	public List<User> getListOfUserCollaboratorId() {
		return listOfUserCollaboratorId;
	}
	
	public void setListOfUserCollaboratorId(List<User> listOfUserCollaboratorId) {
		this.listOfUserCollaboratorId = listOfUserCollaboratorId;
	}
*/	 
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name ="Note_Label",joinColumns = {
			@JoinColumn(name="noteId",nullable = false)},
			inverseJoinColumns = {@JoinColumn(name="labelId",nullable = false)})
	@JsonIgnoreProperties(value = "notes")
	private List<Label> labels = new ArrayList<>();
	
	public List<Label> getLabels() {
		return labels;
	}
	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getNoteId() {
		return noteId;
	}
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public String getColor() {
		return color;
	}
	public String getReminder() {
		return reminder;
	}
	public boolean isTrash() {
		return isTrash;
	}
	public boolean isArchive() {
		return isArchive;
	}
	public boolean isPin() {
		return isPin;
	}
	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public void setReminder(String reminder) {
		this.reminder = reminder;
	}
	public void setTrash(boolean isTrash) {
		this.isTrash = isTrash;
	}
	public void setArchive(boolean isArchive) {
		this.isArchive = isArchive;
	}
	public void setPin(boolean isPin) {
		this.isPin = isPin;
	}
	public Date getCreationTimeStamp() {
		return creationTimeStamp;
	}
	public Date getUpdateTimeStamp() {
		return updateTimeStamp;
	}
	public void setCreationTimeStamp(Date creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}
	public void setUpdateTimeStamp(Date updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}	
}
