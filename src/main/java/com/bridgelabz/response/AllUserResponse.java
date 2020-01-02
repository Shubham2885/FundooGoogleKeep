package com.bridgelabz.response;

import java.util.List;
import java.util.Optional;

import com.bridgelabz.model.User;

public class AllUserResponse {

	//bad request
		public static final Integer STATUS400=400;
		//ok
		public static final Integer STATUS200=200;
		//not found
		public static final Integer STATUS404=404;
		//internal server error
		public static final Integer STATUS500=500;
		public static final String PASSWORD_DOES_NOT_MATCH="Password Does not Match";
		public static final String USER_ADDED ="User Added Successfully";
		public static final String LOGIN_SUCCESSFULL ="Login Successfull...";
		public static final String LOGIN_UNSUCCESSFULL ="Login Unsuccessfull...invalid username and password";
		public static final String USER_EMAIL_EXIST = "Email Aleady Exist";
		public static final String PASSWORD_CHANGE="Password Change Successfully";
		public static final String INVALID_USERNAME="Invalid Username";
		private Integer status;
		private String message;
		private List<User> userData;

		public AllUserResponse(Integer status, String message, List<User> userData) {
			this.status = status;
			this.message = message;
			this.userData = userData;
		}

		public String getMessage() {
			return message;
		}

		public Integer getStatus() {
			return status;
		}

		public List<User> getUserData() {
			return userData;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public void setUserData(List<User> userData) {
			this.userData = userData;
		}
}
