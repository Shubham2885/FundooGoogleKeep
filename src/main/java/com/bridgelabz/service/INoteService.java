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

import com.bridgelabz.dto.NoteDto;
import com.bridgelabz.dto.ReminderDateDto;
import com.bridgelabz.response.Response;

public interface INoteService {

	Response create(NoteDto noteDto,String token) throws IOException;
	Response delete(int noteId,String token) throws IOException;
	Response update(NoteDto noteDto,int noteId,String token);
	Response getAllNotes(String token) throws IOException;
	Response isArchive(int noteId,String token);
	Response isTrash(int noteId,String token)throws IOException;
	Response isPin(int noteId,String token)throws IOException;
	Response setReminder(String token,ReminderDateDto reminderDateDto,int noteId) throws IOException;
	Response editReminder(String token,ReminderDateDto reminderDateDto,int noteId) throws IOException;
	Response removeReminder(String token,int noteId) throws IOException;
	Response getAllNoteInTrash(String token);
	Response getAllNoteInArchive(String token);
	Response sortNoteByTitle(String token);
	Response sortNoteByDescription(String token);
	Response sortNoteByDate(String token);
	Response collaborateUserToNote(String token,int userId,int noteId) throws IOException;
	Response removeMySelf(String token,int noteId);
	Response deleteCollaborateUser(String token,int noteId,int userId);
	Response searchNoteByTitle(String token,String title) throws IOException;
	Response searchNoteByDescription(String token,String description) throws IOException;
	Response searchNoteByText(String token,String text) throws IOException;
}
