/******************************************************************************
 *  Compilation:  javac -d bin NoteCollaborateDto.java
 *  Execution:    java -cp bin com.bridgelabz.dto;
 *  						  
 *  
 *  Purpose:      This class hold collaborate note data for transfering data to controller
 *  @author  Shubham Chavan
 *  @version 1.0
 *  @since   30-12-2019
 *
 ******************************************************************************/
package com.bridgelabz.dto;

import java.util.ArrayList;
import java.util.List;

import com.bridgelabz.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class NoteCollaborateDto {

	private int noteId;
	private String title;
	private String description;
	private String color;
	private boolean isArchive;
	private boolean isPin;
	private String reminder;
	private List<String> collaborateUser = new ArrayList<>();
	
	public List<String> getCollaborateUser() {
		return collaborateUser;
	}
	public void setCollaborateUser(List<String> collaborateUser) {
		this.collaborateUser = collaborateUser;
	}
	public String getReminder() {
		return reminder;
	}
	public void setReminder(String reminder) {
		this.reminder = reminder;
	}
	public boolean isArchive() {
		return isArchive;
	}
	public boolean isPin() {
		return isPin;
	}
	public void setArchive(boolean isArchive) {
		this.isArchive = isArchive;
	}
	public void setPin(boolean isPin) {
		this.isPin = isPin;
	}
	private User user;
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
	
	@JsonIgnoreProperties({"firstName","lastName","email","mobileNumber","active","password","creationTimeStamp","updateTimeStamp"})
	public User getUser() {
		return user;
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
	public void setUser(User user) {
		this.user = user;
	}
}
