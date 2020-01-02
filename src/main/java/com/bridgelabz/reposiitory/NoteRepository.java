package com.bridgelabz.reposiitory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgelabz.model.Notes;

public interface NoteRepository extends JpaRepository<Notes, Integer>{
	List<Notes> findByUserId(int userId); 	
}
