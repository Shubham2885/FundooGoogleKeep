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
package com.bridgelabz.utility;

public class ResponseMessages {

	public static final Integer STATUS400=400;
	//ok
	public static final Integer STATUS200=200;
	//not found
	public static final Integer STATUS404=404;
	//internal server error
	public static final Integer STATUS500=500;
	
	//user related messages
	public static final String PASSWORD_DOES_NOT_MATCH="Password Does not Match";
	public static final String USER_ADDED ="User Added Successfully";
	public static final String LOGIN_SUCCESSFULL ="Login Successfull...";
	public static final String LOGIN_UNSUCCESSFULL ="Login Unsuccessfull...invalid username and password";
	public static final String USER_EMAIL_EXIST = "Email Aleady Exist";
	public static final String PASSWORD_CHANGE="Password Change Successfully";
	public static final String INVALID_USERNAME="Invalid Username";
	public static final String ALL_USERS="All Users : ";
	public static final String VERIFY_ACCOUNT_FIRST="Please verify account first";
	public static final String ACCOUNT_ACTIVATED="Account Activated...you can do login...";
	public static final String USER_DETAILS="User Details : ";
	public static final String USER_NOT_AVAILABLE="User Not Available!";
	// label related messages
	public static final String LABEL_CREATED="Label Created";
	public static final String INVALID_LABEL_ID="Label Id Invalid";
	public static final String LABEL_ADDED_TO_NOTE="Label Added To Note : ";
	public static final String NOTE_UPDATED="Note Updated successfully";
	public static final String ALL_LABELS="All Labels : ";
	public static final String LABEL_REMOVED="Label Removed : ";
	public static final String LABEL_DELETED="Label Deleted : ";
	public static final String LABEL_NOT_AVAILABLE="Label Not Available";
	public static final String INVALID_NOTE_AND_LABEL_ID="Invalid NoteId or LabelId";
	// note related messages
	public static final String NOTE_CREATED="Note Created";
	public static final String USER_COLLABORATE_TO_NOTE="User Collaborate to Note";
	public static final String NOTE_NOT_IN_TRASH="Note does not present into trash";
	public static final String LABEL_RENAMED="Label Renamed";
	public static final String ALL_NOTES="All Notes : ";
	public static final String ALL_ARCHIEVE="Notes In Archive :";
	public static final String ALL_TRASH="Notes In Trash :";
	public static final String NOTE_IN_TRASH="Note is in Trash";
	public static final String NOTE_DELETED="Note Deleted : ";
	public static final String NOTE_ARCHIVED="Note is Archived";
	public static final String NOTE_UNARCHIVED="Note is UnArchived";
	public static final String NOTE_PINNED="Note is Archived";
	public static final String NOTE_UNPIN="Note is UnArchived";
	public static final String INVALID_REMINDER_DATE="Reminder Date should be Today after";
	public static final String REMINDER_NOT_SET="Reminder Not Set";
	public static final String REMINDER_REMOVE="Reminder deleted";
	public static final String INVALID_DATE_FORMAT="Invalid Date format : xx-xx-xxxx";
	public static final String DATE_FORMAT="dd-mm-yyyy";
	public static final String NOTE_SORT_BY_TITLE="Notes Sort By Title ";
	public static final String NOTE_SORT_BY_DATE="Notes Sort By Date ";
	public static final String NOTE_SORT_BY_DESCRIPTION="Notes Sort By Description ";
	public static final String USER_DELETED_FROM_COLLABORATED_NOTE="Delete User from Collaborate";
	public static final String INVALID_NOTE_ID="Invalid note id";
	
}
