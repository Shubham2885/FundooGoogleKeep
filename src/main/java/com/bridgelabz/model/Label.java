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
	private User user = new User();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name ="Note_Label", joinColumns = {
			@JoinColumn(name = "labelId",nullable = false)},
			inverseJoinColumns = {
					@JoinColumn(name="noteId",nullable = false)})
	@JsonIgnoreProperties(value = "labels")
	private List<Notes> notes = new ArrayList<>();
	
	public List<Notes> getNotes() {
		return notes;
	}
	public void setNotes(List<Notes> notes) {
		this.notes = notes;
	}
	public int getLabelId() {
		return labelId;
	}
	public String getLabel() {
		return label;
	}
	public User getUser() {
		return user;
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
}
