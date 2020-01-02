package com.bridgelabz.dto;

import java.util.Date;

import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import net.bytebuddy.implementation.bytecode.Throw;

public class UserDTO {

	@NotEmpty
	@Size(min = 2, max = 30, message = "must be 3 charecter")
	@Pattern(regexp = "^[a-zA-Z]+$", message = "Invalid First Name")
	private String firstName;
	@NotEmpty
	@Size(min = 2, max = 30, message = "must be 3 charecter")
	@Pattern(regexp = "^[a-zA-Z]+$", message = "Invalid Last Name")
	private String lastName;
	@NotEmpty
	@Pattern(regexp = "^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$", message = "Invalid Email Address")
	private String email;
	@NotEmpty
	@Pattern(regexp = "^(0|91)?[7-9][0-9]{9}$", message = "Invalid Mobile Number")
	private String mobileNumber;
	@NotEmpty
	@Pattern(regexp = "^((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{5,15})$", message = "Password should Contain between 5 and 15 long,Contain at least one digitat,one lower case,one upper case and special character from [@#$%!.]")
	private String password;
	@Transient
	@NotEmpty
	@Pattern(regexp = "^((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{5,15})$", message = "Check Password should Contain between 5 and 15 long,Contain at least one digitat,one lower case,one upper case and special character from [@#$%!.]")
	private String checkPassword;

	public UserDTO() {
		// TODO Auto-generated constructor stub
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

	public String getEmail() {
		return email;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public String getPassword() {
		return password;
	}
	public String getCheckPassword() {
		return checkPassword;
	}

	
	public void setEmail(String email) {
		this.email = email;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setCheckPassword(String checkPassword) {
		this.checkPassword = checkPassword;
	}
}
