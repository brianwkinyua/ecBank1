package com.EclecticsInterview.bank1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.EclecticsInterview.bank1.model.Dependant;

@Repository
public interface DependantRepo extends CrudRepository<Dependant, Integer>{

	public List<Dependant> findAll(); 
//	public List<Dependant> findByPerson(Person person); 
	public List<Dependant> findByPersonId(int personId);
	public Dependant findById(int id); 
	
}
