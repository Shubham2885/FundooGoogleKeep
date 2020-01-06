package com.bridgelabz.response;

import java.util.List;

import com.bridgelabz.model.Label;
import com.bridgelabz.model.Note;

public class LabelResponse {

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
		private List<Label> labels;

		public LabelResponse(Integer status, String message, List<Label> labels) {
			this.status = status;
			this.message = message;
			this.labels = labels;
		}

		public String getMessage() {
			return message;
		}

		public Integer getStatus() {
			return status;
		}

		public List<Label> getLabels() {
			return labels;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public void setLabels(List<Label> labels) {
			this.labels = labels;
		}
}
