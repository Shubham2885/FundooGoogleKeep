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
import javax.validation.constraints.Size;

public class LabelDto {

	@NotEmpty
	@Size(min = 2, max = 100, message = "must be 3 to 100 charecters")
	@Pattern(regexp = "^[a-z A-Z]+$", message = "Invalid Title Name")
	private String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
