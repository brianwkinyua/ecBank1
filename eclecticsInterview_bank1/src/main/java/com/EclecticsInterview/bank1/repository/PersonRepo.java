package com.EclecticsInterview.bank1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.EclecticsInterview.bank1.model.Person;

@Repository
public interface PersonRepo extends CrudRepository<Person, Integer>{

	public List<Person> findAll(); 
	public Person findById(int id); 
	
}
