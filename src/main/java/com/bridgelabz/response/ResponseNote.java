package com.bridgelabz.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.bridgelabz.model.Label;
import com.bridgelabz.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class ResponseNote {
	private int noteId;
	private String title;
	private String description;
	private String color;
	private String reminder;
	private boolean isTrash = false;
	private boolean isArchive = false;
	private boolean isPin = false;
	
	
	private Date creationTimeStamp;
	
	private Date updateTimeStamp;

	 
	private int userId;
	
	private List<Label> labels = new ArrayList<>();
	
	public List<Label> getLabels() {
		return labels;
	}
	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
