/******************************************************************************
 *  Compilation:  javac -d bin RenameLabelDto.java
 *  Execution:    java -cp bin com.bridgelabz.dto;
 *  						  
 *  
 *  Purpose:      This class hold details of label and transfer to service
 *  @author  Shubham Chavan
 *  @version 1.0
 *  @since   02-01-2020
 *
 ******************************************************************************/
package com.bridgelabz.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RenameLabelDto {

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
