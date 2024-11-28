package com.EclecticsInterview.bank1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.EclecticsInterview.bank1.model.PersonDocument;

@Repository
public interface PersonDocumentRepo extends CrudRepository<PersonDocument, Integer>{

	public List<PersonDocument> findAll(); 
//	public List<PersonDocument> findByPerson(Person person); 
	public List<PersonDocument> findByPersonId(int personId);
	public PersonDocument findById(int id); 
	
}
