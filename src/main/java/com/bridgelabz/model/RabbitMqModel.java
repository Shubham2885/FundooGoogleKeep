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
package com.bridgelabz.model;

public class RabbitMqModel {

	String subject;
	String sendTo;
	String text;
	
	public RabbitMqModel(String subject, String sendTo, String text) {
		this.subject = subject;
		this.sendTo = sendTo;
		this.text = text;
	}
	public String getSubject() {
		return subject;
	}
	public String getSendTo() {
		return sendTo;
	}
	public String getText() {
		return text;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}
	public void setText(String text) {
		this.text = text;
	}
}
