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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserCollaborate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int collaborateId;

	@ManyToOne
	@JoinColumn(name = "userId", nullable = false, updatable = true)
	private User user = new User();

	@ManyToOne
	@JoinColumn(name = "noteId", nullable = false)
	private Note note = new Note();

	private boolean isArchive;
	private boolean isPin;
	private String reminder;
	
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

	public User getUser() {
		return user;
	}

	public Note getNote() {
		return note;
	}

	public int getCollaborateId() {
		return collaborateId;
	}

	public void setCollaborateId(int collaborateId) {
		this.collaborateId = collaborateId;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setNote(Note note) {
		this.note = note;
	}
}
