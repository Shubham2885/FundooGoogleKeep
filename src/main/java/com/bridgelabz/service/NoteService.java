package com.bridgelabz.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.dto.NoteCollaborateDto;
import com.bridgelabz.dto.NoteDto;
import com.bridgelabz.dto.ReminderDateDto;
import com.bridgelabz.exception.custome.DateParseException;
import com.bridgelabz.exception.custome.NoteNotAvailable;
import com.bridgelabz.exception.custome.UserNotFoundException;
import com.bridgelabz.model.Note;
import com.bridgelabz.model.User;
import com.bridgelabz.model.UserCollaborate;
import com.bridgelabz.reposiitory.NoteRepository;
import com.bridgelabz.reposiitory.UserCollaborateRepository;
import com.bridgelabz.reposiitory.UserRepository;
import com.bridgelabz.response.NoteResponse;
import com.bridgelabz.response.NoteResponse2;
import com.bridgelabz.response.UserResponse;
import com.bridgelabz.utility.JwtUtil;

@Service
public class NoteService {

	private ModelMapper mapper = new ModelMapper();
	@Autowired
	private UserCollaborateRepository userCollaborateRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private NoteRepository noteRepository;
	@Autowired 
	private JwtUtil jwtUtil;
	private ModelMapper modelMapper = new ModelMapper();
	
	/**
	 * @purpose : create a new note 
	 * @param noteDto : store note info
	 * @param token : current user token
	 * @return : OK message
	 */
	public NoteResponse create(NoteDto noteDto,String token){
		Note note = modelMapper.map(noteDto, Note.class);
		note.setUser(getUser(token));
		noteRepository.save(note);
		return new NoteResponse(UserResponse.STATUS200, "Note is added", null);
	}
	
	/**
	 * @purpose : delete note
	 * @param noteId : which note we want to delete
	 * @param token : current user token
	 * @return : OK message
	 */
	public NoteResponse delete(int noteId,String token){
		if(noteId>0){
			Note note = getNote(noteId, token);
			if(note.isTrash()) {
			noteRepository.delete(note);
			return new NoteResponse(NoteResponse.STATUS200, "Note deleted successfully", null);
			}
			return new NoteResponse(NoteResponse.STATUS200, "Note does not present into trash", null);
		}
		return new NoteResponse(NoteResponse.STATUS400,"Invalid Note Id", null);
	}
	
	/**
	 * @purpose : update a note 
	 * @param noteDto : store updated info
	 * @param noteId : which note we want to update
	 * @param token : current user token
	 * @return : OK Message
	 */
	public NoteResponse update(NoteDto noteDto,int noteId,String token){
		if(noteId>0){
			UserCollaborate collaborateNote=getCollaborateNote(token, noteId);
			if(collaborateNote!=null) {
				updateNote(collaborateNote.getNote(), 1, noteDto, collaborateNote);
			}else {
				updateNote(getNote(noteId, token), 0, noteDto, collaborateNote);
			}
			return new NoteResponse(NoteResponse.STATUS200, "Note Updated successfully", null);
		}
		return new NoteResponse(NoteResponse.STATUS400,"Invalid Note Id", null);
	}
	
	/**
	 * @purpose : update a note
	 * @param note : particular note which is going to update
	 * @param type : which type of note we want to update like 1 is collaborator note or 0 is user note
	 * @param noteDto : store update info
	 * @param collaborateNote : collaborate note
	 */
	private void updateNote(Note note,int type,NoteDto noteDto,UserCollaborate collaborateNote) {
		note.setTitle(noteDto.getTitle());
		note.setDescription(noteDto.getDescription());
		note.setColor(noteDto.getColor());
		note.setReminder(noteDto.getReminder());
		noteRepository.save(note);
		if(type==1) {
			collaborateNote.setNote(note);
			userCollaborateRepository.save(collaborateNote);
		}
	}
	
	/**
	 * @purpose : return all notes of the current user
	 * @param token : current user token
	 * @return : all notes
	 */
	public NoteResponse2 getAllNotes(String token){	
		List<Note> notes= getListOfNote(token);
		notes = notes.stream().filter(list1->!list1.isArchive() && !list1.isTrash()).collect(Collectors.toList());
		User user = getUser(token);
		List<UserCollaborate> collaborateList = userCollaborateRepository.findAll().stream().filter(list1->list1.getUser().equals(user) && !list1.isArchive()).collect(Collectors.toList());
		return new NoteResponse2(NoteResponse.STATUS200, "Notes : ",setAllCollaborateNote(collaborateList, notes));
	}
	
	/**
	 * @purpose : do a note Archive or UnArchive
	 * @param noteId : note id which we are going to Archive or UnArchive
	 * @param token : current user token
	 * @return : return OK message
	 */
	public NoteResponse isArchive(int noteId,String token){
		if(noteId>0){
			UserCollaborate collaborateNote=getCollaborateNote(token, noteId);
			if(collaborateNote!=null) {
				return new NoteResponse(NoteResponse.STATUS200, setArchiveValue(collaborateNote, 1), null);
			}else {
			Note note = getNote(noteId, token);
			if(note.isTrash())
				return new NoteResponse(NoteResponse.STATUS400,"Note is in Trash", null);
			return new NoteResponse(NoteResponse.STATUS200, setArchiveValue(note, 0), null);
			}
		}
		return new NoteResponse(NoteResponse.STATUS400,"Invalid Note Id", null);
	}
	
	/**
	 * @purpose : set Archive value
	 * @param obj : Store objects of note or collaborateNote
	 * @param type : 1 set pin for collaborateNote and 0 set pin for note
	 * @return : return pin message
	 */
	private String setArchiveValue(Object obj,int type) {
		String message ="";
		if(type==1) {
			UserCollaborate collaborateNote = (UserCollaborate)obj;
			if(collaborateNote.isArchive()) {
				collaborateNote.setArchive(false);
				message = "Note NuArchived";
			}else {
				collaborateNote.setArchive(true);
				collaborateNote.setPin(false);
				message = "Note Archived";
			}
			userCollaborateRepository.save(collaborateNote);
		}else {
			Note note = (Note)obj;
			if(note.isArchive()) {
				note.setArchive(false);
				message = "Note NuArchived";
			}else {
				note.setArchive(true);
				note.setPin(false);
				message = "Note Archived";
			}
			noteRepository.save(note);
		}
		return message;
	}
	/**
	 * @purpose : do a note goes into trash or restore from trash
	 * @param noteId : note id which we are going to trashed or restore
	 * @param token : current user token
	 * @return : return OK message
	 */
	public NoteResponse isTrash(int noteId,String token){
		if(noteId>0){
			
			Note note = getNote(noteId, token);
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
	
	/**
	 * @purpose : do a note pin or unpin
	 * @param noteId : note id which we are going to pin or unpin
	 * @param token : current user token
	 * @return : return OK message
	 */
	public NoteResponse isPin(int noteId,String token) {
		if(noteId>0){
			UserCollaborate collaborateNote=getCollaborateNote(token, noteId);
			if(collaborateNote!=null) {
				return new NoteResponse(NoteResponse.STATUS200, setPinValue(collaborateNote, 1), null);
			}else {
			Note note = getNote(noteId, token);
			if(note.isTrash())
				return new NoteResponse(NoteResponse.STATUS400,"Note may be in Trash", null);
			return new NoteResponse(NoteResponse.STATUS200, setPinValue(note, 0), null);
			}
		}
		return new NoteResponse(NoteResponse.STATUS400,"Invalid Note Id", null);
	}
	
	/**
	 * @purpose : setPinValue
	 * @param obj : Store objects of note or collaborateNote
	 * @param type : 1 set pin for collaborateNote and 0 set pin for note
	 * @return : return pin message
	 */
	private String setPinValue(Object obj,int type) {
		String message ="";
		if(type==1) {
			UserCollaborate collaborateNote=(UserCollaborate)obj;
			if(collaborateNote.isPin()) {
				collaborateNote.setPin(false);
				message = "Note UnPin";
			}else {
				collaborateNote.setPin(true);
				collaborateNote.setArchive(false);
				message = "Note Pinned";
			}
			userCollaborateRepository.save(collaborateNote);
		}else {
			Note note = (Note)obj;
			if(note.isPin()) {
				note.setPin(false);
				message = "Note UnPin";
			}else {
				note.setPin(true);
				note.setArchive(false);
				message = "Note Pinned";
			}
			noteRepository.save(note);
		}
		return message;
	}
	
	/**
	 * @purpose : set reminder
	 * @param token : current user token
	 * @param reminderDateDto : store reminder date
	 * @param noteId : note id which we are going to set reminder
	 * @return : return OK message
	 */
	public NoteResponse setReminder(String token,ReminderDateDto reminderDateDto,int noteId) {
		Date currentDate=null,reminderDate=null;
		if(noteId<=0)
			return new NoteResponse(NoteResponse.STATUS200, "Invalid Note Id", null);;
		SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
		try {
			 currentDate = format.parse(currentDate());
			 reminderDate = format.parse(reminderDateDto.getDate());
		}catch (ParseException e) {
			throw new DateParseException("Invalid Date format : xx-xx-xxxx");
		}
		if(reminderDate.before(currentDate))
			return new NoteResponse(NoteResponse.STATUS200, "Reminder Date should be Today after", null);
		UserCollaborate collaborateNote=getCollaborateNote(token, noteId);
		if(collaborateNote!=null) {
			collaborateNote.setReminder(reminderDateDto.getDate());
			userCollaborateRepository.save(collaborateNote);
		}else {
			Note note = getNote(noteId, token);
			note.setReminder(reminderDateDto.getDate());
			noteRepository.save(note);	
		}
		return new NoteResponse(NoteResponse.STATUS200, "Reminder is set on "+reminderDateDto.getDate()+" at 8 AM", null);
	}
	
	/**
	 * @purpose : remove reminder on note
	 * @param token : current user token
	 * @param noteId : note id which we are going to remove reminder
	 * @return : OK message
	 */
	public NoteResponse removeReminder(String token,int noteId) {
		UserCollaborate collaborateNote=getCollaborateNote(token, noteId);
		if(collaborateNote!=null) {
			collaborateNote.setReminder("Not Set");
			userCollaborateRepository.save(collaborateNote);
		}else {
			Note note = getNote(noteId, token);
			note.setReminder("Not Set");
			noteRepository.save(note);	
		}
		return new NoteResponse(NoteResponse.STATUS200, "Reminder deleted", null);
	}
	/**
	 * @purpose : return all notes in the trash of particular current user
	 * @param token : current user token
	 * @return : return all notes in the trash
	 */
	public NoteResponse getAllNoteInTrash(String token) {
		List<Note> notes = getListOfNote(token);
		notes = notes.stream().filter(note->!note.isArchive() && note.isTrash()).collect(Collectors.toList());
		return new NoteResponse(NoteResponse.STATUS200,"Notes in Trash ", notes);
	}
	
	/**
	 * @purpose : return all notes in the archive of particular current user
	 * @param token : current user token
	 * @return : return all notes in the archive
	 */
	public NoteResponse2 getAllNoteInArchive(String token) {
		List<Note> notes = getListOfNote(token);
		notes = notes.stream().filter(note->note.isArchive() && !note.isTrash()).collect(Collectors.toList());
		User user = getUser(token);
		List<UserCollaborate> collaborateList = userCollaborateRepository.findAll().stream().filter(list1->list1.getUser().equals(user) && list1.isArchive()).collect(Collectors.toList());
		return new NoteResponse2(NoteResponse.STATUS200,"Notes in Archive ", setAllCollaborateNote(collaborateList, notes));
	}
	
	/**
	 * @purpose : Notes sorted by Title
	 * @param token : current user token
	 * @return : sorted list
	 */
	public NoteResponse sortNoteByTitle(String token) {
		List<Note> notes = getListOfNote(token);
		notes = notes.stream().sorted((note1,note2)->note1.getTitle().compareTo(note2.getTitle())).collect(Collectors.toList());
		return new NoteResponse(NoteResponse.STATUS200,"Notes Sort By Title ", notes);
	}
	
	/**
	 * @purpose : Notes sorted by Description
	 * @param token : current user token
	 * @return : sorted list
	 */
	public NoteResponse sortNoteByDescription(String token) {
		List<Note> notes = getListOfNote(token);
		notes = notes.stream().sorted((note1,note2)->note1.getDescription().compareTo(note2.getDescription())).collect(Collectors.toList());
		return new NoteResponse(NoteResponse.STATUS200,"Notes Sort By Description ", notes);
	}
	
	/**
	 * @purpose : Notes sorted by Date
	 * @param token : current user token
	 * @return : sorted list
	 */
	public NoteResponse sortNoteByDate(String token) {
		List<Note> notes = getListOfNote(token);
		notes = notes.stream().sorted((note1,note2)->note1.getCreationTimeStamp().compareTo(note2.getCreationTimeStamp())).collect(Collectors.toList());
		return new NoteResponse(NoteResponse.STATUS200,"Notes Sort By Date ", notes);
	}
	
	/**
	 * @purpose : collaborate a note with the other users
	 * @param token : current user token
	 * @param userId : other user id
	 * @param noteId : note id which is going to collaborate with other user
	 * @return : OK message
	 */
	public NoteResponse collaborateUserToNote(String token,int userId,int noteId) {
		if(userId<=0 || noteId<=0)
			return new NoteResponse(NoteResponse.STATUS200,"Invalid User Id or Note Id", null);
		Note note = getNote(noteId, token);
		Optional<User> user = userRepository.findById(userId);
		UserCollaborate userCollaborate = new UserCollaborate();
		userCollaborate.setNote(note);
		userCollaborate.setUser(user.get());
		userCollaborateRepository.save(userCollaborate);
		noteRepository.save(note);
		return new NoteResponse(NoteResponse.STATUS200,"User Collaborate to Note", null);
	}
	
	/**
	 * @purpose : return list of Notes of perticular user
	 * @param token : current user token
	 * @return : return list of Notes
	 */
	private List<Note> getListOfNote(String token){
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
	private Note getNote(int noteId,String token){
		List<Note> listOfNote = noteRepository.findByUserId(getUser(token).getId());
		try {
			Note note =(listOfNote.stream().filter(n->n.getNoteId()==noteId).collect(Collectors.toList())).get(0);	
			return note;
		}catch (NoteNotAvailable e) {
			throw new NoteNotAvailable("Note Not Available");
		}catch (IndexOutOfBoundsException e) {
			throw new NoteNotAvailable("Note Not Available");
		}
	}
	
	/**
	 * @purpose : return current date
	 * @return : current date
	 */
	private String currentDate() {
		return LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString();
	}
	
	/**
	 * @purpose : return note which is collaborated with current user
	 * @param token : current user token
	 * @param noteId : note id
	 * @return : return collaborated note
	 */
	private UserCollaborate getCollaborateNote(String token,int noteId) {
		List<UserCollaborate> collaborateList = userCollaborateRepository.findAll().stream().filter(list1->list1.getUser().equals(getUser(token))).collect(Collectors.toList());
		try {
			 return ((collaborateList.stream().filter(list->list.getNote().getNoteId()==noteId)).collect(Collectors.toList())).get(0);	
		}catch (Exception e) {}
		return null;
	}
	
	/**
	 * @purpose : set archive and pin value to NoteCollaborateDto and get all collaborate notes
	 * @param collaborateList : Collaborate users list
	 * @param notes : current user notes
	 * @return : return all notes
	 */
	private List<Object> setAllCollaborateNote(List<UserCollaborate> collaborateList,List<Note> notes){
		List<NoteCollaborateDto> collaborateNote = new ArrayList<>();
		for(UserCollaborate collaborate:collaborateList) {
			NoteCollaborateDto noteCollaborate = mapper.map(collaborate.getNote(),NoteCollaborateDto.class );
			noteCollaborate.setArchive(collaborate.isArchive());
			noteCollaborate.setPin(collaborate.isPin());
			noteCollaborate.setReminder(collaborate.getReminder());
			collaborateNote.add(noteCollaborate);
		}
		List<Object> notesObjects = new ArrayList<>();
		notesObjects.add(collaborateNote);
		notesObjects.add(notes);
		return notesObjects;
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