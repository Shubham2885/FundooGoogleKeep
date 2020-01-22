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

public class LoginDTO {

	@NotEmpty
	@Pattern(regexp = "^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$", message = "Invalid Email Address")
	private String email;
	@NotEmpty
	@Pattern(regexp = "^((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{5,15})$", message = "Password should Contain between 5 and 15 long,Contain at least one digitat,one lower case,one upper case and special character from [@#$%!.]")
	private String password;
	
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
