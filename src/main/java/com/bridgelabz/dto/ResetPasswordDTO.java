package com.bridgelabz.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class ResetPasswordDTO {

	@NotEmpty
	@Pattern(regexp = "^((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{5,15})$", message = "Password should Contain between 5 and 15 long,Contain at least one digitat,one lower case,one upper case and special character from [@#$%!.]")
	private String password;
	@NotEmpty
	@Pattern(regexp = "^((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{5,15})$", message = "Password should Contain between 5 and 15 long,Contain at least one digitat,one lower case,one upper case and special character from [@#$%!.]")
	private String checkPassword;

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCheckPassword() {
		return checkPassword;
	}
	public void setCheckPassword(String checkPassword) {
		this.checkPassword = checkPassword;
	}
	
}
