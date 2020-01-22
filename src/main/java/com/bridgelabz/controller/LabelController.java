/******************************************************************************
 *  Compilation:  javac -d bin LabelController.java
 *  Execution:    java -cp bin com.bridgelabz.controller;
 *  						  
 *  
 *  Purpose:      This Label Controller class
 *  @author  Shubham Chavan
 *  @version 1.0
 *  @since   29-12-2019
 *
 ******************************************************************************/
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
import com.bridgelabz.response.Response;
import com.bridgelabz.service.ILabelService;

@RestController
@RequestMapping("/fundoo")
public class LabelController {

	@Autowired
	ILabelService labelService;

	@PostMapping("/note/addLabel")
	public Response addLabel(@RequestHeader String token,@RequestParam int noteId,@RequestParam int labelId){
		return labelService.addLabelToNote(token, noteId, labelId);
	}
	
	@DeleteMapping("/deleteLabel")
	public Response deleteLabel(@RequestHeader String token,@RequestParam int labelId){
		return labelService.deleteLabel(token, labelId);
	}
	
	@PutMapping("/renameLabel")
	public Response updateLabel(@Valid @RequestBody RenameLabelDto renameLabelDto,@RequestHeader String token,@RequestParam int labelId){
		return labelService.renameLabel(renameLabelDto, token, labelId);
	}
	
	@PostMapping("/createLabel")
	public Response createLabel(@Valid @RequestBody LabelDto labelDto,@RequestHeader String token){
		return labelService.createLabel(token, labelDto);
	}
	
	@GetMapping("/getAllLabel")
	public Response getAllLabel(@RequestHeader String token) {
		return labelService.getAllLabel(token);
	}
	
	@PutMapping("/note/removeLabelOnNote")
	public Response removeLabelOnNote(@RequestHeader String token,@RequestParam int noteId,@RequestParam int labelId) {
		return labelService.removeLabelOnNote(token, noteId, labelId);
	}
}
