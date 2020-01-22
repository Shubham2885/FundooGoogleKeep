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

import java.io.Serializable;

public class RedisModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String email;
	private String lastLogin;
	public String getId() {
		return id;
	}
	public String getEmail() {
		return email;
	}
	public String getLastLogin() {
		return lastLogin;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
}
