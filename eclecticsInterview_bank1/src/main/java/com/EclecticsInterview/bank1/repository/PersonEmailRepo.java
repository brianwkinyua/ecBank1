package com.EclecticsInterview.bank1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.EclecticsInterview.bank1.model.PersonEmail;


@Repository
public interface PersonEmailRepo extends CrudRepository<PersonEmail, Integer>{

	public List<PersonEmail> findAll(); 
//	public List<PersonEmail> findByPerson(Person person); 
	public List<PersonEmail> findByPersonId(int personId); 
	public PersonEmail findById(int id); 
	
}
