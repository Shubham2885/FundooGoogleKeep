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
package com.bridgelabz.exception.custome;

public class CustomException {

	public static class BadCredintial extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public BadCredintial(String message) {
			super(message);
		}
	}
	
	public static class DateParseException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public DateParseException(String msg) {
			super(msg);
		}
	}
	
	public static class ExpiredJwtTokenExeception extends RuntimeException{
		private static final long serialVersionUID = 1L;

		public ExpiredJwtTokenExeception(String msg) {
			super(msg);
		}
	}
	
	public static class LabelNotFound extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public LabelNotFound(String msg) {
			super(msg);
		}
	}
	
	public static class NoteNotAvailable extends RuntimeException{
		private static final long serialVersionUID = 1L;

		public NoteNotAvailable(String msg) {
			super(msg);
		}
	}
	
	public static class UserAleadyExist extends RuntimeException{
		private static final long serialVersionUID = 1L;

		public UserAleadyExist(String message) {
			super(message);
		}
	}
	
	public static class UserNotFoundException extends RuntimeException{
		private static final long serialVersionUID = 1L;

		public UserNotFoundException(String message) {
			super(message);
		}
	}
	
	public static class MailError extends RuntimeException{
		private static final long serialVersionUID = 1L;
		public MailError(String message) {
			super(message);
		}
	}
	
	public static class UserNotLogin extends RuntimeException{
		private static final long serialVersionUID = 1L;
		public UserNotLogin(String message) {
			super(message);
		}
	}
}
