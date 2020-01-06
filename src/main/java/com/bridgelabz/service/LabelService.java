package com.bridgelabz.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.dto.LabelDto;
import com.bridgelabz.dto.RenameLabelDto;
import com.bridgelabz.exception.custome.LabelNotFound;
import com.bridgelabz.exception.custome.NoteNotAvailable;
import com.bridgelabz.exception.custome.UserNotFoundException;
import com.bridgelabz.model.Label;
import com.bridgelabz.model.Note;
import com.bridgelabz.model.User;
import com.bridgelabz.reposiitory.LabelRepository;
import com.bridgelabz.reposiitory.NoteRepository;
import com.bridgelabz.reposiitory.UserRepository;
import com.bridgelabz.response.LabelResponse;
import com.bridgelabz.utility.JwtUtil;

@Service
public class LabelService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	JwtUtil jwtUtil;
	@Autowired
	NoteRepository noteRepository;
	@Autowired
	LabelRepository labelRepository;
	private ModelMapper modelMapper = new ModelMapper();
	
	/**
	 * @purpose : create a new label for user
	 * @param token : current user token
	 * @param labelDto : it store info of label
	 * @return : return ok message
	 */
	public LabelResponse createLabel(String token,LabelDto labelDto){
		Label label = modelMapper.map(labelDto, Label.class);
		label.setUser(getUser(token));
		//label.setUserId(getUser(token).getId());
		labelRepository.save(label);
		return new LabelResponse(LabelResponse.STATUS200, "Label Created", null);
	}
	
	/**
	 * @purpose : add a new label on the note
	 * @param token : current user token
	 * @param noteId : used for find a note
	 * @param labelId : used for find a label
	 * @return : return ok message
	 */
	public LabelResponse addLabelToNote(String token,int noteId,int labelId) {
		Label label = getLabel(token, labelId);
		Note note = getNote(noteId, token);
		label.getNotes().add(note);
		labelRepository.save(label);
		return new LabelResponse(LabelResponse.STATUS200, "Label Added", null);
	}
	
	/**
	 * @purpose : rename of a label of user
	 * @param renameLabelDto : new name of the label
	 * @param token : current user token
	 * @param labelId : labelId used for find a label
	 * @return : return ok message
	 */
	public LabelResponse renameLabel(RenameLabelDto renameLabelDto,String token,int labelId) {
		if(labelId<=0)
			return new LabelResponse(LabelResponse.STATUS400, "Label Id Invalid", null);
		
		Label label = getLabel(token, labelId);
		label.setLabel(renameLabelDto.getLabel());
		labelRepository.save(label);
		return new LabelResponse(LabelResponse.STATUS200, "Label Renamed", null);	
	}
	
	
	/**
	 * @purpose : return the list of the labels of user
	 * @param token : current user token
	 * @return : return list of labels
	 */
	public LabelResponse getAllLabel(String token){
		return new LabelResponse(LabelResponse.STATUS200, "Labels", getLabels(token));
	}
	
	/**
	 * @purpose : remove a label on a note of user
	 * @param token : current user token
	 * @param noteId : noteId used for find a note
	 * @param labelId : labelId used for find a label
	 * @return : return remove message
	 */
	public LabelResponse removeLabelOnNote(String token,int noteId,int labelId) {
		if(noteId<=0 || labelId<=0)
			return new LabelResponse(LabelResponse.STATUS200, "Invalid NoteId or LabelId", null);
		Label label = getLabel(token, labelId);
//		Notes note = getNote(noteId, token);
		List<Note> notes=label.getNotes();
		notes.removeIf(i->i.getNoteId()==noteId);
		label.setNotes(notes);
		labelRepository.save(label);
		return new LabelResponse(LabelResponse.STATUS200, "Remove Label", null);
	}
	
	public LabelResponse deleteLabel(String token,int labelId) {
		if(labelId<=0)
			return new LabelResponse(LabelResponse.STATUS200, "Invalid NoteId or LabelId", null);
		Label label = getLabel(token,labelId);
		List<Note> notes = label.getNotes();
		notes.forEach(list->list.getLabels().removeIf(i->i.getLabelId()==labelId));
		label.setNotes(notes);
		labelRepository.delete(label);
		return new LabelResponse(LabelResponse.STATUS200, "Label Delete", null);
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
		}catch (LabelNotFound e) {
			throw new LabelNotFound("Label Not Found");
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
		return userRepository.findByEmail(jwtUtil.validateToken(token)).orElseThrow(()->new UserNotFoundException("User Not Found"));
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
		}catch (NoteNotAvailable e) {
			throw new NoteNotAvailable("Note Not Available");
		}catch (IndexOutOfBoundsException e) {
			throw new NoteNotAvailable("Note Not Available");
		}
	}

}
