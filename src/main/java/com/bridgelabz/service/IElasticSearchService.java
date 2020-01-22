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

import java.io.IOException;
import java.util.List;

import com.bridgelabz.model.Note;
import com.bridgelabz.response.Response;

public interface IElasticSearchService {

	Response insert(Note note) throws IOException;
	Response delete(Note note) throws IOException;
	List<Note> searchNoteById(String id) throws IOException;
	Response update(Note note) throws IOException;
	Response searchNoteByTitle(String title,int userId) throws IOException;
	Response searchNoteByDescription(String description,int userId) throws IOException;
	Response searchNoteByText(String text,int userId) throws IOException;
	Response findAll(int userId) throws IOException;
}
