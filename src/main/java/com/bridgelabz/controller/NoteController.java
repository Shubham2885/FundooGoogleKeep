package com.bridgelabz.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.bridgelabz.response.NoteResponse;
import com.bridgelabz.response.UserResponse;
import com.bridgelabz.service.NoteService;

@RestController
@RequestMapping("/fundoo/note")
public class NoteController {

	@Autowired
	private NoteService noteService;
	@PostMapping("/create")
	public NoteResponse create(@Valid @RequestBody NoteDto noteDto,@RequestHeader String token)
	{
		return noteService.create(noteDto, token);
	}
	
	@DeleteMapping("/delete")
	public NoteResponse delete(@RequestParam int noteId,@RequestHeader String token)
	{
		return noteService.delete(noteId,token);
	}
	
	@PutMapping("/update")
	public NoteResponse update(@Valid @RequestBody NoteDto noteDto,@RequestParam int noteId,@RequestHeader String token)
	{
		return noteService.update(noteDto, noteId,token);
	}
	
	@GetMapping("/allNote")
	public NoteResponse getAllNote(@RequestHeader String token)
	{
		return noteService.getAllNotes(token);
	}
	
	@GetMapping("/isArchive")
	public NoteResponse isArchive(@RequestParam int noteId,@RequestHeader String token)
	{
		return noteService.isArchive(noteId,token);
	}
	
	@GetMapping("/isTrash")
	public NoteResponse isTrash(@RequestParam int noteId,@RequestHeader String token)
	{
		return noteService.isTrash(noteId,token);
	}
	
	@GetMapping("/isPin")
	public NoteResponse isPin(@RequestParam int noteId,@RequestHeader String token)
	{
		return noteService.isPin(noteId,token);
	}
	
	@GetMapping("/getAllInTrash")
	public NoteResponse getAllInTrash(@RequestHeader String token) {
		return noteService.getAllNoteInTrash(token);
	}
	
	@GetMapping("/getAllInArchive")
	public NoteResponse getAllInArchive(@RequestHeader String token) {
		return noteService.getAllNoteInArchive(token);
	}
}
