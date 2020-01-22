/******************************************************************************
 *  Compilation:  javac -d bin NoteController.java
 *  Execution:    java -cp bin com.bridgelabz.controller;
 *  						  
 *  
 *  Purpose:      This Note controller class
 *  @author  Shubham Chavan
 *  @version 1.0
 *  @since   29-12-2019
 *
 ******************************************************************************/
package com.bridgelabz.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.dto.NoteDto;
import com.bridgelabz.dto.ReminderDateDto;
import com.bridgelabz.response.Response;
import com.bridgelabz.service.INoteService;

@RestController
@RequestMapping("/fundoo/note")
public class NoteController {
	
	// private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private INoteService noteService;
	@PostMapping("/create")
	public Response create(@Valid @RequestBody NoteDto noteDto,@RequestHeader String token) throws IOException
	{
		 //LOG.info("Getting user with ID {}.", token);
		return noteService.create(noteDto, token);
	}
	
	@DeleteMapping("/delete")
	public Response delete(@RequestParam int noteId,@RequestHeader String token) throws IOException
	{
		return noteService.delete(noteId,token);
	}
	
	@PutMapping("/update")
	public Response update(@Valid @RequestBody NoteDto noteDto,@RequestParam int noteId,@RequestHeader String token)
	{
		return noteService.update(noteDto, noteId,token);
	}
	
	@GetMapping("/allNote")
	public Response getAllNote(@RequestHeader String token) throws IOException
	{
		return noteService.getAllNotes(token);
	}
	
	@GetMapping("/isArchive")
	public Response isArchive(@RequestParam int noteId,@RequestHeader String token)
	{
		return noteService.isArchive(noteId,token);
	}
	
	@GetMapping("/isTrash")
	public Response isTrash(@RequestParam int noteId,@RequestHeader String token) throws IOException
	{
		return noteService.isTrash(noteId,token);
	}
	
	@GetMapping("/isPin")
	public Response isPin(@RequestParam int noteId,@RequestHeader String token) throws IOException
	{
		return noteService.isPin(noteId,token);
	}
	
	@GetMapping("/getAllInTrash")
	public Response getAllInTrash(@RequestHeader String token) {
		return noteService.getAllNoteInTrash(token);
	}
	
	@GetMapping("/getAllInArchive")
	public Response getAllInArchive(@RequestHeader String token) {
		return noteService.getAllNoteInArchive(token);
	}
	
	@PutMapping("/setReminder")
	public Response setReminder(@RequestHeader String token,@RequestBody ReminderDateDto reminderDateDto,@RequestParam int noteId) throws IOException {
		return noteService.setReminder(token, reminderDateDto, noteId);
	}
	
	@GetMapping("/removeReminder")
	public Response removeReminder(@RequestHeader String token,@RequestParam int noteId) throws IOException {
		return noteService.removeReminder(token, noteId);
	}
	
	@PutMapping("/editReminder")
	public Response editReminder(@RequestHeader String token,@RequestBody ReminderDateDto reminderDateDto,@RequestParam int noteId) throws IOException {
		return noteService.editReminder(token, reminderDateDto, noteId);
	}
	
	@GetMapping("/sortNoteByTitle")
	public Response sortNoteByTitle(@RequestHeader String token) {
		return noteService.sortNoteByTitle(token);
	}
	
	@GetMapping("/sortNoteByDescription")
	public Response sortNoteByDiscription(@RequestHeader String token) {
		return noteService.sortNoteByDescription(token);
	}
	
	@GetMapping("/sortNoteByDate")
	public Response sortNoteByDate(@RequestHeader String token) {
		return noteService.sortNoteByDate(token);
	}
	
	@GetMapping("/collaborateUserToNote")
	public Response collaborateUserToNote(@RequestHeader String token,@RequestParam int userId,@RequestParam int noteId) throws IOException {
		return noteService.collaborateUserToNote(token, userId, noteId);
	}
	
	@DeleteMapping("/removeMySelfFromCollaborateNote")
	public Response removeMySelfFromCollaborateNote(@RequestHeader String token,@RequestParam int noteId) {
		return noteService.removeMySelf(token, noteId);
	}
	
	@DeleteMapping("/deleteCollaborateUser")
	public Response deleteCollaborateUser(@RequestHeader String token,@RequestParam int noteId,@RequestParam int userId) {
		return noteService.deleteCollaborateUser(token, noteId, userId);
	}
	
	@GetMapping("/searchNoteByTitle")
	public Response searchNoteByTitle(@RequestHeader String token,@RequestParam String title) throws IOException {
		return noteService.searchNoteByTitle(token, title);
	}
	
	@GetMapping("/searchNoteByDescription")
	public Response searchNoteByDescription(@RequestHeader String token,@RequestParam String description) throws IOException {
		return noteService.searchNoteByDescription(token, description);
	}
	
	@GetMapping("/searchNoteByText")
	public Response searchNoteByText(@RequestHeader String token,@RequestParam String text) throws IOException {
		return noteService.searchNoteByText(token, text);
	}
}
