/******************************************************************************
 *  Compilation:  javac -d bin ReminderDateDto.java
 *  Execution:    java -cp bin com.bridgelabz.dto;
 *  						  
 *  
 *  Purpose:      This class validate date of reminder
 *  @author  Shubham Chavan
 *  @version 1.0
 *  @since   02-01-2020
 *
 ******************************************************************************/
package com.bridgelabz.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class ReminderDateDto {

	@NotEmpty(message = "Reminder date should not empty")
	@Pattern(regexp = "^([0-2][0-9]|(3)[0-1])(\\-)(((0)[0-9])|((1)[0-2]))(\\-)\\d{4}$", message = "Invalid Date, Date format should be xx/xx/xxxx")
	private String date;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
