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
package com.bridgelabz.response;

import java.io.Serializable;

public class Response implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer status;
	private String message;
	private Object obj;
	public Integer getStatus() {
		return status;
	}
	public String getMessage() {
		return message;
	}
	public Object getObj() {
		return obj;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public Response(Integer status, String message, Object obj) {
		super();
		this.status = status;
		this.message = message;
		this.obj = obj;
	}
}
