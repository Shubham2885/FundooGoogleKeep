package com.bridgelabz.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.dto.NoteDto;
import com.bridgelabz.exception.custome.NoteNotAvailable;
import com.bridgelabz.exception.custome.UserNotFoundException;
import com.bridgelabz.model.Notes;
import com.bridgelabz.model.User;
import com.bridgelabz.reposiitory.NoteRepository;
import com.bridgelabz.reposiitory.UserRepository;
import com.bridgelabz.response.NoteResponse;
import com.bridgelabz.response.UserResponse;
import com.bridgelabz.utility.JwtUtil;

@Service
public class NoteService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private NoteRepository noteRepository;
	@Autowired 
	private JwtUtil jwtUtil;
	private ModelMapper modelMapper = new ModelMapper();
	
	public NoteResponse create(NoteDto noteDto,String token){
		Notes notes = modelMapper.map(noteDto, Notes.class);
		notes.setUser(getUser(token));
		noteRepository.save(notes);
		return new NoteResponse(UserResponse.STATUS200, "Note is added", null);
	}
	
	public NoteResponse delete(int noteId,String token){
		if(noteId>0){
			Notes note = getNote(noteId, token);
			if(note.isTrash()) {
			noteRepository.delete(note);
			return new NoteResponse(NoteResponse.STATUS200, "Note deleted successfully", null);
			}
			return new NoteResponse(NoteResponse.STATUS200, "Note does not present into trash", null);
		}
		return new NoteResponse(NoteResponse.STATUS400,"Invalid Note Id", null);
	}
	
	public NoteResponse update(NoteDto noteDto,int noteId,String token){
		if(noteId>0){
			Notes note = getNote(noteId, token);
			note.setTitle(noteDto.getTitle());
			note.setDescription(noteDto.getDescription());
			note.setColor(noteDto.getColor());
			note.setReminder(noteDto.getReminder());
			noteRepository.save(note);
			return new NoteResponse(NoteResponse.STATUS200, "Note Updated successfully", null);
		}
		return new NoteResponse(NoteResponse.STATUS400,"Invalid Note Id", null);
	}
	
	public NoteResponse getAllNotes(String token){	
		List<Notes> list= getListOfNote(token);
		list = list.stream().filter(list1->!list1.isArchive() && !list1.isTrash()).collect(Collectors.toList());
  		return new NoteResponse(NoteResponse.STATUS200, "Notes : ",list);
	}
	
	public NoteResponse isArchive(int noteId,String token){
		if(noteId>0){
			Notes note = getNote(noteId, token);
			if(note.isTrash())
				return new NoteResponse(NoteResponse.STATUS400,"Note is in Trash", null);
			String message ="";
			if(note.isArchive()) {
				note.setArchive(false);
				note.setPin(false);
				message = "Note NuArchived";
			}else {
				note.setArchive(true);
				message = "Note Archived";
			}
			noteRepository.save(note);
			return new NoteResponse(NoteResponse.STATUS200, message, null);
		}
		return new NoteResponse(NoteResponse.STATUS400,"Invalid Note Id", null);
	}
	
	public NoteResponse isTrash(int noteId,String token){
		if(noteId>0){
			Notes note = getNote(noteId, token);
			if(note.isArchive())
				return new NoteResponse(NoteResponse.STATUS400,"Note is Archived", null);
			String message ="";
			if(note.isTrash()) {
				note.setTrash(false);
				note.setPin(false);
				message = "Note Restored";
			}else {
				note.setTrash(true);
				message = "Note in Trash";
			}
			noteRepository.save(note);
			return new NoteResponse(NoteResponse.STATUS200, message, null);
		}
		return new NoteResponse(NoteResponse.STATUS400,"Invalid Note Id", null);
	}
	
	public NoteResponse isPin(int noteId,String token) {
		if(noteId>0){
			Notes note = getNote(noteId, token);
			if(note.isArchive() || note.isTrash())
				return new NoteResponse(NoteResponse.STATUS400,"Note may be in Archive or Trash", null);
			String message ="";
			if(note.isPin()) {
				note.setPin(false);
				message = "Note UnPin";
			}else {
				note.setPin(true);
				message = "Note Pinned";
			}
			noteRepository.save(note);
			return new NoteResponse(NoteResponse.STATUS200, message, null);
		}
		return new NoteResponse(NoteResponse.STATUS400,"Invalid Note Id", null);
	}
	
	/**
	 * @purpose : return all notes in the trash of particular current user
	 * @param token : current user token
	 * @return : return all notes in the trash
	 */
	public NoteResponse getAllNoteInTrash(String token) {
		List<Notes> notes = getListOfNote(token);
		notes = notes.stream().filter(note->!note.isArchive() && note.isTrash()).collect(Collectors.toList());
		return new NoteResponse(NoteResponse.STATUS200,"Notes in Trash ", notes);
	}
	
	/**
	 * @purpose : return all notes in the archive of particular current user
	 * @param token : current user token
	 * @return : return all notes in the archive
	 */
	public NoteResponse getAllNoteInArchive(String token) {
		List<Notes> notes = getListOfNote(token);
		notes = notes.stream().filter(note->note.isArchive() && !note.isTrash()).collect(Collectors.toList());
		return new NoteResponse(NoteResponse.STATUS200,"Notes in Archive ", notes);
	}
	
	/**
	 * @purpose : return list of Notes of perticular user
	 * @param token : current user token
	 * @return : return list of Notes
	 */
	private List<Notes> getListOfNote(String token){
		return noteRepository.findByUserId(getUser(token).getId());
	}
	
	/**
	 * @Purpose : this method return the current user details
	 * @param token : it is login token
	 * @return : current user details
	 */
	private User getUser(String token) {
		return userRepository.findByEmail(jwtUtil.validateToken(token)).orElseThrow(()->new UserNotFoundException("User Not Found"));
	}
	
	/**
	 * @purpose : this method return the perticular note of the perticular user using note id
	 * @param noteId : note id of current user
	 * @param token : token of current user
	 * @return : return the note
	 */
	private Notes getNote(int noteId,String token){
		List<Notes> listOfNote = noteRepository.findByUserId(getUser(token).getId());
		try {
			Notes note =(listOfNote.stream().filter(n->n.getNoteId()==noteId).collect(Collectors.toList())).get(0);	
			return note;
		}catch (NoteNotAvailable e) {
			throw new NoteNotAvailable("Note Not Available");
		}catch (IndexOutOfBoundsException e) {
			throw new NoteNotAvailable("Note Not Available");
		}
	}
}

/*
 * //List<Notes> list = noteRepository.findByUserId(user.getId());
		//System.out.println("list of notes"+list);
		//List<Notes> value=list.stream().filter(list1->list1.getUser().getId() user.getId()).collect(Collectors.toList());
*/

/*
List<Notes> notes=noteRepository.findAll().stream()
		.filter(note-> note.getUser().getId()==user.getId()).collect(Collectors.toList());
 */
	//notes.forEach(note->note.setUser(null));