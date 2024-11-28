package com.EclecticsInterview.bank1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.EclecticsInterview.bank1.model.PersonPhone;


@Repository
public interface PersonPhoneRepo extends CrudRepository<PersonPhone, Integer>{

	public List<PersonPhone> findAll(); 
//	public List<PersonPhone> findByPerson(Person person); 
	public List<PersonPhone> findByPersonId(int personId); 
	public PersonPhone findById(int id); 
	
}
