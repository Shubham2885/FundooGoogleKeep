package com.bridgelabz.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name ="User")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private String mobileNumber;
	private boolean Active;
	//@OneToMany(cascade = CascadeType.REMOVE,mappedBy = "user")
	//private List<Notes> notes = new ArrayList<>();
	@CreationTimestamp
	private Date creationTimeStamp;
	@UpdateTimestamp
	private Date updateTimeStamp;

/*	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="User_Collaborator",joinColumns = {
			@JoinColumn(name="id",nullable = false)},inverseJoinColumns = {
					@JoinColumn(name="noteId",nullable = false)})
	@JsonIgnoreProperties(value = "listOfUserCollaboratorId")
	private List<Note> listOfNoteId = new ArrayList<>();
	
	
//	public List<Notes> getNotes() {
//		return notes;
//	}
//
//	public void setNotes(List<Notes> notes) {
//		this.notes = notes;
//	}

	public List<Note> getListOfNoteId() {
		return listOfNoteId;
	}

	public void setListOfNoteId(List<Note> listOfNoteId) {
		this.listOfNoteId = listOfNoteId;
	}
*/
	public int getId() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}
	public String getEmail() {
		return email;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	
	public Date getCreationTimeStamp() {
		return creationTimeStamp;
	}
	public Date getUpdateTimeStamp() {
		return updateTimeStamp;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public boolean isActive() {
		return Active;
	}
	public void setActive(boolean active) {
		this.Active = active;
	}
	public void setCreationTimeStamp(Date creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}
	public void setUpdateTimeStamp(Date updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}
}
