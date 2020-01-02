package com.bridgelabz.reposiitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgelabz.model.Label;

public interface LabelRepository extends JpaRepository<Label, Integer>{
	List<Label> findByUserId(int id);
}
