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

import com.bridgelabz.dto.LabelDto;
import com.bridgelabz.dto.RenameLabelDto;
import com.bridgelabz.response.LabelResponse;
import com.bridgelabz.service.LabelService;

@RestController
@RequestMapping("/fundoo")
public class LabelController {

	@Autowired
	LabelService labelService;

	@PostMapping("/note/addLabel")
	public LabelResponse addLabel(@RequestHeader String token,@RequestParam int noteId,@RequestParam int labelId){
		return labelService.addLabelToNote(token, noteId, labelId);
	}
	
	@DeleteMapping("/deleteLabel")
	public LabelResponse deleteLabel(){
		return null;
	}
	
	@PutMapping("/renameLabel")
	public LabelResponse updateLabel(@Valid @RequestBody RenameLabelDto renameLabelDto,@RequestHeader String token,@RequestParam int labelId){
		return labelService.renameLabel(renameLabelDto, token, labelId);
	}
	
	@GetMapping("/createLabel")
	public LabelResponse createLabel(@Valid @RequestBody LabelDto labelDto,@RequestHeader String token){
		return labelService.createLabel(token, labelDto);
	}
	
	@GetMapping("/getAllLabel")
	public LabelResponse getAllLabel(@RequestHeader String token) {
		return labelService.getAllLabel(token);
	}
	
	@PutMapping("/note/removeLabelOnNote")
	public LabelResponse removeLabelOnNote(@RequestHeader String token,@RequestParam int noteId,@RequestParam int labelId) {
		return labelService.removeLabelOnNote(token, noteId, labelId);
	}
}
