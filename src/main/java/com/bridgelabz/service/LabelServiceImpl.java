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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.dto.LabelDto;
import com.bridgelabz.dto.RenameLabelDto;
import com.bridgelabz.exception.custome.CustomException;
import com.bridgelabz.model.Label;
import com.bridgelabz.model.Note;
import com.bridgelabz.model.RedisModel;
import com.bridgelabz.model.User;
import com.bridgelabz.reposiitory.IRedisRepository;
import com.bridgelabz.reposiitory.LabelRepository;
import com.bridgelabz.reposiitory.NoteRepository;
import com.bridgelabz.reposiitory.UserRepository;
import com.bridgelabz.response.Response;
import com.bridgelabz.utility.JwtUtil;
import com.bridgelabz.utility.ResponseMessages;

@Service
public class LabelServiceImpl implements ILabelService{

	@Autowired
	private UserRepository userRepository;
	@SuppressWarnings("unused")
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private NoteRepository noteRepository;
	@Autowired
	private LabelRepository labelRepository;
	
	@Autowired
	private IRedisRepository redisRepository;
	private ModelMapper modelMapper = new ModelMapper();
	
	/**
	 * @purpose : create a new label for user
	 * @param token : current user token
	 * @param labelDto : it store info of label
	 * @return : return ok message
	 */
	@Override
	public Response createLabel(String token,LabelDto labelDto){
		Label label = modelMapper.map(labelDto, Label.class);
		label.setUser(getUser(token));
		//label.setUserId(getUser(token).getId());
		labelRepository.save(label);
		return new Response(ResponseMessages.STATUS200, ResponseMessages.LABEL_CREATED, null);
	}
	
	/**
	 * @purpose : add a new label on the note
	 * @param token : current user token
	 * @param noteId : used for find a note
	 * @param labelId : used for find a label
	 * @return : return ok message
	 */
	@Override
	public Response addLabelToNote(String token,int noteId,int labelId) {
		Label label = getLabel(token, labelId);
		Note note = getNote(noteId, token);
		label.getNotes().add(note);
		labelRepository.save(label);
		return new Response(ResponseMessages.STATUS200,ResponseMessages.LABEL_ADDED_TO_NOTE , null);
	}
	
	/**
	 * @purpose : rename of a label of user
	 * @param renameLabelDto : new name of the label
	 * @param token : current user token
	 * @param labelId : labelId used for find a label
	 * @return : return ok message
	 */
	@Override
	public Response renameLabel(RenameLabelDto renameLabelDto,String token,int labelId) {
		if(labelId<=0)
			return new Response(ResponseMessages.STATUS400, ResponseMessages.INVALID_LABEL_ID, null);
		
		Label label = getLabel(token, labelId);
		label.setLabel(renameLabelDto.getLabel());
		labelRepository.save(label);
		return new Response(ResponseMessages.STATUS200, ResponseMessages.LABEL_RENAMED, null);	
	}
	
	
	/**
	 * @purpose : return the list of the labels of user
	 * @param token : current user token
	 * @return : return list of labels
	 */
	@Override
	public Response getAllLabel(String token){
		return new Response(ResponseMessages.STATUS200,ResponseMessages.ALL_LABELS , getLabels(token));
	}
	
	/**
	 * @purpose : remove a label on a note of user
	 * @param token : current user token
	 * @param noteId : noteId used for find a note
	 * @param labelId : labelId used for find a label
	 * @return : return remove message
	 */
	@Override
	public Response removeLabelOnNote(String token,int noteId,int labelId) {
		if(noteId<=0 || labelId<=0)
			return new Response(ResponseMessages.STATUS200,ResponseMessages.INVALID_NOTE_AND_LABEL_ID , null);
		Label label = getLabel(token, labelId);
//		Notes note = getNote(noteId, token);
		List<Note> notes=label.getNotes();
		notes.removeIf(i->i.getNoteId()==noteId);
		label.setNotes(notes);
		labelRepository.save(label);
		return new Response(ResponseMessages.STATUS200, ResponseMessages.LABEL_REMOVED, null);
	}
	
	public Response deleteLabel(String token,int labelId) {
		if(labelId<=0)
			return new Response(ResponseMessages.STATUS200, ResponseMessages.INVALID_LABEL_ID, null);
		Label label = getLabel(token,labelId);
		List<Note> notes = label.getNotes();
		notes.forEach(list->list.getLabels().removeIf(i->i.getLabelId()==labelId));
		label.setNotes(notes);
		labelRepository.delete(label);
		return new Response(ResponseMessages.STATUS200, ResponseMessages.LABEL_DELETED, null);
	}
	
	/**
	 * @purpose : return a label of user
	 * @param token : current user token
	 * @param labelId : labelId used for find the label
	 * @return : return a label
	 */
	private Label getLabel(String token,int labelId) {
		try {
			return (getLabels(token).stream().filter(label->label.getLabelId()==labelId).collect(Collectors.toList())).get(0);
		}catch (CustomException.LabelNotFound e) {
			throw new CustomException.LabelNotFound(ResponseMessages.LABEL_NOT_AVAILABLE);
		}
	}
	
	/**
	 * @purpose  return all label of user
	 * @param token : current user token
	 * @return : return list of label
	 */
	private List<Label> getLabels(String token){
		return labelRepository.findByUserId(getUser(token).getId());
	}
	
	/**
	 * @Purpose : this method return the current user details
	 * @param token : it is login token
	 * @return : current user details
	 */
	private User getUser(String token) {
		Optional<User> user =userRepository.findByEmail(checkUserLogin(token));
		return user.get();
		//return userRepository.findByEmail(jwtUtil.validateToken(token)).orElseThrow(()->new CustomException.UserNotFoundException(ResponseMessages.USER_NOT_AVAILABLE));
	}
	
	/**
	 * @purpose : this method return the particular note of the particular user using note id
	 * @param noteId : note id of current user
	 * @param token : token of current user
	 * @return : return the note
	 */
	private Note getNote(int noteId,String token){
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
	
	private String checkUserLogin(String token) {
		RedisModel model = null;
		model=redisRepository.findUser(token);
		if(model==null)
			throw new CustomException.UserNotLogin("Yor are not login...please do login");
		return model.getEmail();
	}

}
