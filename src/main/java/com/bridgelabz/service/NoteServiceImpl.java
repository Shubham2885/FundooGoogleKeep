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
import com.bridgelabz.exception.custome.CustomException;
import com.bridgelabz.model.Note;
import com.bridgelabz.model.RedisModel;
import com.bridgelabz.model.User;
import com.bridgelabz.model.UserCollaborate;
import com.bridgelabz.reposiitory.IRedisRepository;
import com.bridgelabz.reposiitory.NoteRepository;
import com.bridgelabz.reposiitory.UserCollaborateRepository;
import com.bridgelabz.reposiitory.UserRepository;
import com.bridgelabz.response.Response;
import com.bridgelabz.utility.JwtUtil;
import com.bridgelabz.utility.ResponseMessages;

@Service
public class NoteServiceImpl implements INoteService{

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
	@Autowired
	private IElasticSearchService elasticSearchService;
	@Autowired
	private IRedisRepository redisRepository; 
	/**
	 * @purpose : create a new note 
	 * @param noteDto : store note info
	 * @param token : current user token
	 * @return : OK message
	 * @throws IOException 
	 */
	@Override
	public Response create(NoteDto noteDto,String token) throws IOException{
		Note note = modelMapper.map(noteDto, Note.class);
		note.setUser(getUser(token));
		noteRepository.save(note);
		elasticSearchService.insert(note);
		return new Response(ResponseMessages.STATUS200, ResponseMessages.NOTE_CREATED, null);
	}
	
	/**
	 * @purpose : delete note
	 * @param noteId : which note we want to delete
	 * @param token : current user token
	 * @return : OK message
	 * @throws IOException 
	 */
	@Override
	public Response delete(int noteId,String token) throws IOException{
			Note note = getNote(noteId, token);
			System.out.println(elasticSearchService.searchNoteById(Integer.toString(noteId)));
			if(note.isTrash()) {
				noteRepository.delete(note);
				elasticSearchService.delete(note);
			return new Response(ResponseMessages.STATUS200, ResponseMessages.NOTE_DELETED, null);
			}
			return new Response(ResponseMessages.STATUS200, ResponseMessages.NOTE_NOT_IN_TRASH, null);
	}
	
	/**
	 * @purpose : update a note 
	 * @param noteDto : store updated info
	 * @param noteId : which note we want to update
	 * @param token : current user token
	 * @return : OK Message
	 */
	@Override
	public Response update(NoteDto noteDto,int noteId,String token){
		
			UserCollaborate collaborateNote=getCollaborateNote(token, noteId);
			if(collaborateNote!=null) {
				updateNote(collaborateNote.getNote(), 1, noteDto, collaborateNote);
			}else {
				updateNote(getNote(noteId, token), 0, noteDto, collaborateNote);
			}
			return new Response(ResponseMessages.STATUS200, ResponseMessages.NOTE_UPDATED, null);
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
	 * @throws IOException 
	 */
	
	@Override
	public Response getAllNotes(String token) throws IOException{	
		List<Note> notes= getListOfNote(token);
		notes = notes.stream().filter(list1->!list1.isArchive() && !list1.isTrash()).collect(Collectors.toList());
		notes = setCollaborateUser(notes);
		User user = getUser(token);
		List<UserCollaborate> collaborateList = userCollaborateRepository.findAll().stream().filter(list1->list1.getUser().equals(user) && !list1.isArchive()).collect(Collectors.toList());
		return new Response(ResponseMessages.STATUS200, "Notes : ",setAllCollaborateNote(collaborateList, notes));
	}
	
	/**
	 * @purpose : return all notes of the current user
	 * @param token : current user token
	 * @return : all notes
	 * @throws IOException 
	 */
	//by using elastic search
	/*
	@Override
	public Response getAllNotes(String token) throws IOException{	
		User user = getUser(token);
		return new Response(ResponseMessages.STATUS200, ResponseMessages.ALL_NOTES,elasticSearchService.findAll(user.getId()));
	}
	*/
	/**
	 * @purpose : do a note Archive or UnArchive
	 * @param noteId : note id which we are going to Archive or UnArchive
	 * @param token : current user token
	 * @return : return OK message
	 */
	@Override
	public Response isArchive(int noteId,String token){
		
			UserCollaborate collaborateNote=getCollaborateNote(token, noteId);
			if(collaborateNote!=null) {
				return new Response(ResponseMessages.STATUS200, setArchiveValue(collaborateNote, 1), null);
			}else {
			Note note = getNote(noteId, token);
			if(note.isTrash())
				return new Response(ResponseMessages.STATUS400,ResponseMessages.NOTE_IN_TRASH, null);
			return new Response(ResponseMessages.STATUS200, setArchiveValue(note, 0), null);
			}
	}
	
	/**
	 * @purpose : set Archive value
	 * @param obj : Store objects of note or collaborateNote
	 * @param type : 1 set pin for collaborateNote and 0 set pin for note
	 * @return : return pin message
	 */
	
	private String setArchiveValue(Object obj, int type) {
		String message ="";
		if(type==1) {
			UserCollaborate collaborateNote = (UserCollaborate)obj;
			if(collaborateNote.isArchive()) {
				collaborateNote.setArchive(false);
				message = ResponseMessages.NOTE_UNARCHIVED;
			}else {
				collaborateNote.setArchive(true);
				collaborateNote.setPin(false);
				message = ResponseMessages.NOTE_ARCHIVED;
			}
			userCollaborateRepository.save(collaborateNote);
		}else {
			Note note = (Note)obj;
			if(note.isArchive()) {
				note.setArchive(false);
				message = ResponseMessages.NOTE_UNARCHIVED;
			}else {
				note.setArchive(true);
				note.setPin(false);
				message = ResponseMessages.NOTE_ARCHIVED;
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
	 * @throws IOException 
	 */
	@Override
	public Response isTrash(int noteId, String token) throws IOException{
			Note note = getNote(noteId, token);
			if(note.isArchive())
				return new Response(ResponseMessages.STATUS400,ResponseMessages.NOTE_ARCHIVED, null);
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
			elasticSearchService.update(note);
			return new Response(ResponseMessages.STATUS200, message, null);
	}
	
	/**
	 * @purpose : do a note pin or unpin
	 * @param noteId : note id which we are going to pin or unpin
	 * @param token : current user token
	 * @return : return OK message
	 * @throws IOException 
	 */
	@Override
	public Response isPin(int noteId,String token) throws IOException {
			UserCollaborate collaborateNote=getCollaborateNote(token, noteId);
			if(collaborateNote!=null) {
				return new Response(ResponseMessages.STATUS200, setPinValue(collaborateNote, 1), null);
			}else {
			Note note = getNote(noteId, token);
			if(note.isTrash())
				return new Response(ResponseMessages.STATUS400,ResponseMessages.NOTE_IN_TRASH, null);
			return new Response(ResponseMessages.STATUS200, setPinValue(note, 0), null);
			}
	}
	
	/**
	 * @purpose : setPinValue
	 * @param obj : Store objects of note or collaborateNote
	 * @param type : 1 set pin for collaborateNote and 0 set pin for note
	 * @return : return pin message
	 * @throws IOException 
	 */
	
	private String setPinValue(Object obj,int type) throws IOException {
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
			elasticSearchService.update(note);
		}
		return message;
	}
	
	/**
	 * @purpose : set reminder
	 * @param token : current user token
	 * @param reminderDateDto : store reminder date
	 * @param noteId : note id which we are going to set reminder
	 * @return : return OK message
	 * @throws IOException 
	 */
	@Override
	public Response setReminder(String token,ReminderDateDto reminderDateDto,int noteId) throws IOException {
		Date currentDate=(Date)getDates(reminderDateDto).get(0),
				reminderDate=(Date)getDates(reminderDateDto).get(1);
		if(reminderDate.before(currentDate))
			return new Response(ResponseMessages.STATUS200, ResponseMessages.INVALID_REMINDER_DATE, null);
		UserCollaborate collaborateNote=getCollaborateNote(token, noteId);
		if(collaborateNote!=null) {
			collaborateNote.setReminder(reminderDateDto.getDate());
			userCollaborateRepository.save(collaborateNote);
		}else {
			Note note = getNote(noteId, token);
			note.setReminder(reminderDateDto.getDate());
			noteRepository.save(note);	
			elasticSearchService.update(note);
		}
		return new Response(ResponseMessages.STATUS200, "Reminder is set on "+reminderDateDto.getDate()+" at 8 AM", null);
	}
	
	/**
	 * @purpose : set reminder
	 * @param token : current user token
	 * @param reminderDateDto : store reminder date
	 * @param noteId : note id which we are going to set reminder
	 * @return : return OK message
	 * @throws IOException 
	 */
	@Override
	public Response editReminder(String token, ReminderDateDto reminderDateDto,int noteId) throws IOException {
		Date currentDate=(Date)getDates(reminderDateDto).get(0),
				reminderDate=(Date)getDates(reminderDateDto).get(1);
		if(reminderDate.before(currentDate))
			return new Response(ResponseMessages.STATUS200, ResponseMessages.INVALID_REMINDER_DATE, null);
		UserCollaborate collaborateNote=getCollaborateNote(token, noteId);
		if(collaborateNote!=null) {
			if(collaborateNote.getReminder()==null)
				return new Response(ResponseMessages.STATUS200,ResponseMessages.REMINDER_NOT_SET,null);
			collaborateNote.setReminder(reminderDateDto.getDate());
			userCollaborateRepository.save(collaborateNote);
		}else {
			Note note = getNote(noteId, token);
			if(note.getReminder()==null)
				return new Response(ResponseMessages.STATUS200,ResponseMessages.REMINDER_NOT_SET,null);
			note.setReminder(reminderDateDto.getDate());
			noteRepository.save(note);
			elasticSearchService.update(note);
		}
		return new Response(ResponseMessages.STATUS200, "Reminder is set on "+reminderDateDto.getDate()+" at 8 AM", null);
	}
	
	/**
	 * @purpose : remove reminder on note
	 * @param token : current user token
	 * @param noteId : note id which we are going to remove reminder
	 * @return : OK message
	 * @throws IOException 
	 */
	@Override
	public Response removeReminder(String token,int noteId) throws IOException {
		UserCollaborate collaborateNote=getCollaborateNote(token, noteId);
		if(collaborateNote!=null) {
			collaborateNote.setReminder(null);
			userCollaborateRepository.save(collaborateNote);
		}else {
			Note note = getNote(noteId, token);
			note.setReminder(null);
			noteRepository.save(note);
			elasticSearchService.update(note);
		}
		return new Response(ResponseMessages.STATUS200,ResponseMessages.REMINDER_REMOVE , null);
	}
	
	private List<Object> getDates(ReminderDateDto reminderDateDto){
		Date currentDate=null,reminderDate=null;
		SimpleDateFormat format = new SimpleDateFormat(ResponseMessages.DATE_FORMAT);
		try {
			 currentDate = format.parse(currentDate());
			 reminderDate = format.parse(reminderDateDto.getDate());
		}catch (ParseException e) {
			throw new CustomException.DateParseException(ResponseMessages.INVALID_DATE_FORMAT);
		}
		List<Object> list = new ArrayList<>();
		list.add(currentDate);
		list.add(reminderDate);
		return list;
	}
	/**
	 * @purpose : return all notes in the trash of particular current user
	 * @param token : current user token
	 * @return : return all notes in the trash
	 */
	@Override
	public Response getAllNoteInTrash(String token) {
		List<Note> notes = getListOfNote(token);
		notes = notes.stream().filter(note->!note.isArchive() && note.isTrash()).collect(Collectors.toList());
		notes = setCollaborateUser(notes);
		return new Response(ResponseMessages.STATUS200,ResponseMessages.ALL_TRASH, notes);
	}
	
	/**
	 * @purpose : return all notes in the archive of particular current user
	 * @param token : current user token
	 * @return : return all notes in the archive
	 */
	@Override
	public Response getAllNoteInArchive(String token) {
		List<Note> notes = getListOfNote(token);
		notes = notes.stream().filter(note->note.isArchive() && !note.isTrash()).collect(Collectors.toList());
		notes = setCollaborateUser(notes);
		User user = getUser(token);
		List<UserCollaborate> collaborateList = userCollaborateRepository.findAll().stream().filter(list1->list1.getUser().equals(user) && list1.isArchive()).collect(Collectors.toList());
		return new Response(ResponseMessages.STATUS200,ResponseMessages.ALL_ARCHIEVE, setAllCollaborateNote(collaborateList, notes));
	}
	
	/**
	 * @purpose : Notes sorted by Title
	 * @param token : current user token
	 * @return : sorted list
	 */
	@Override
	public Response sortNoteByTitle(String token) {
		List<Note> notes = getListOfNote(token);
		notes = notes.stream().sorted((note1,note2)->note1.getTitle().compareTo(note2.getTitle())).collect(Collectors.toList());
		return new Response(ResponseMessages.STATUS200,ResponseMessages.NOTE_SORT_BY_TITLE, notes);
	}
	
	/**
	 * @purpose : Notes sorted by Description
	 * @param token : current user token
	 * @return : sorted list
	 */
	@Override
	public Response sortNoteByDescription(String token) {
		List<Note> notes = getListOfNote(token);
		notes = notes.stream().sorted((note1,note2)->note1.getDescription().compareTo(note2.getDescription())).collect(Collectors.toList());
		return new Response(ResponseMessages.STATUS200,ResponseMessages.NOTE_SORT_BY_DESCRIPTION, notes);
	}
	
	/**
	 * @purpose : Notes sorted by Date
	 * @param token : current user token
	 * @return : sorted list
	 */
	@Override
	public Response sortNoteByDate(String token) {
		List<Note> notes = getListOfNote(token);
		notes = notes.stream().sorted((note1,note2)->note1.getCreationTimeStamp().compareTo(note2.getCreationTimeStamp())).collect(Collectors.toList());
		return new Response(ResponseMessages.STATUS200,ResponseMessages.NOTE_SORT_BY_DATE, notes);
	}
	
	/**
	 * @purpose : collaborate a note with the other users
	 * @param token : current user token
	 * @param userId : other user id
	 * @param noteId : note id which is going to collaborate with other user
	 * @return : OK message
	 * @throws IOException 
	 */
	@Override
	public Response collaborateUserToNote(String token,int userId,int noteId) throws IOException {
		Note note = getNote(noteId, token);
		Optional<User> user = userRepository.findById(userId);
		UserCollaborate userCollaborate = new UserCollaborate();
		userCollaborate.setNote(note);
		userCollaborate.setUser(user.get());
		userCollaborateRepository.save(userCollaborate);
		noteRepository.save(note);
		elasticSearchService.update(note);
		return new Response(ResponseMessages.STATUS200,ResponseMessages.USER_COLLABORATE_TO_NOTE, null);
	}
	
	/**
	 * @purpose : remove user from collaborated note
	 * @param token : current token user
	 * @param noteId : collaborated note id
	 * @return : OK message
	 */
	@Override
	public Response removeMySelf(String token,int noteId) {
		UserCollaborate collaborateNote = getCollaborateNote(token, noteId);
		userCollaborateRepository.delete(collaborateNote);
		return new Response(ResponseMessages.STATUS200,ResponseMessages.NOTE_DELETED, null);
	}
	
	@Override
	public Response deleteCollaborateUser(String token,int noteId,int userId) {
		UserCollaborate collaborateNote = getCollaborateNote(token, noteId);
		userCollaborateRepository.delete(collaborateNote);
		return new Response(ResponseMessages.STATUS200,ResponseMessages.USER_DELETED_FROM_COLLABORATED_NOTE, null);
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
		Optional<User> user =userRepository.findByEmail(checkUserLogin(token));
		return user.get();
		//return userRepository.findByEmail(jwtUtil.validateToken(token)).orElseThrow(()->new CustomException.UserNotFoundException("User Not Found"));
	}
	
	/**
	 * @purpose : this method return the perticular note of the perticular user using note id
	 * @param noteId : note id of current user
	 * @param token : token of current user
	 * @return : return the note
	 */
	private Note getNote(int noteId,String token){
		checkInvalidNoteId(noteId);
		List<Note> listOfNote = noteRepository.findByUserId(getUser(token).getId());
		try {
			Note note =(listOfNote.stream().filter(n->n.getNoteId()==noteId).collect(Collectors.toList())).get(0);	
			return note;
		}catch (CustomException.NoteNotAvailable e) {
			throw new CustomException.NoteNotAvailable("Note Not Available");
		}catch (IndexOutOfBoundsException e) {
			throw new CustomException.NoteNotAvailable("Note Not Available");
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
		checkInvalidNoteId(noteId);
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
			noteCollaborate.setCollaborateUser(collaboratedUser(collaborate.getNote().getNoteId()));
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
	
	//private List<>
	private Response checkInvalidNoteId(int noteId) {
		if(noteId<=0)
			return new Response(ResponseMessages.STATUS200,ResponseMessages.INVALID_NOTE_ID, null);
		else
			return null;
	}
	
	private List<String> collaboratedUser(int noteId){
		List<UserCollaborate> collaborateList = userCollaborateRepository.findAll().stream().filter(list1->list1.getNote().getNoteId()==noteId).collect(Collectors.toList());
		List<String> listOfCollaboratedUser = new ArrayList<>();
		for(UserCollaborate user:collaborateList) {
			listOfCollaboratedUser.add(user.getUser().getEmail());
		}
		return listOfCollaboratedUser;
	}
	
	private List<Note> setCollaborateUser(List<Note> notes){
		List<Note> notess = new ArrayList<>();
		for(Note note:notes) {
			note.setCollaborateUser(collaboratedUser(note.getNoteId()));
			notess.add(note);
		}
		return notess;
	}
	
	public Response searchNoteByTitle(String token,String title) throws IOException {
		return elasticSearchService.searchNoteByTitle(title, getUser(token).getId());
	}
	
	public Response searchNoteByDescription(String token,String description) throws IOException {
		return elasticSearchService.searchNoteByDescription(description, getUser(token).getId());
	}
	
	public Response searchNoteByText(String token,String text) throws IOException {
		return elasticSearchService.searchNoteByText(text, getUser(token).getId());
	}
	
	private String checkUserLogin(String token) {
		RedisModel model = null;
		model=redisRepository.findUser(token);
		if(model==null)
			throw new CustomException.UserNotLogin("Yor are not login...please do login");
		return model.getEmail();
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