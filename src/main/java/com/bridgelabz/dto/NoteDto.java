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
package com.bridgelabz.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class NoteDto {

	@NotEmpty
	@Size(min = 2, max = 100, message = "must be 3 to 100 charecters")
	@Pattern(regexp = "^[a-z A-Z]+$", message = "Invalid Title Name")
	private String title;
	@NotEmpty
	@Size(min = 2, max = 1000, message = "must be 3 to 1000 charecters")
	private String description;
	private String color;
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public String getColor() {
		return color;
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
}
