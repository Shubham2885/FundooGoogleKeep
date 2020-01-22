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
package com.bridgelabz.service;

import com.bridgelabz.dto.LabelDto;
import com.bridgelabz.dto.RenameLabelDto;
import com.bridgelabz.response.Response;

public interface ILabelService {
	Response createLabel(String token,LabelDto labelDto);
	Response addLabelToNote(String token,int noteId,int labelId);
	Response renameLabel(RenameLabelDto renameLabelDto,String token,int labelId);
	Response getAllLabel(String token);
	Response removeLabelOnNote(String token,int noteId,int labelId);
	Response deleteLabel(String token,int labelId);
}
