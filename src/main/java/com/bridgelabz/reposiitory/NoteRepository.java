package com.bridgelabz.reposiitory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgelabz.model.Note;

public interface NoteRepository extends JpaRepository<Note, Integer>{
	List<Note> findByUserId(int userId); 	
}
