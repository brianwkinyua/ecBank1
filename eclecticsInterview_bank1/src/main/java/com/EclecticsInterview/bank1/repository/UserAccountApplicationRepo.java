package com.EclecticsInterview.bank1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.EclecticsInterview.bank1.model.UserAccountApplication;


@Repository
public interface UserAccountApplicationRepo extends CrudRepository<UserAccountApplication, Integer>{

	public List<UserAccountApplication> findAll(); 
	public List<UserAccountApplication> findByPersonId(int personId); 
	public UserAccountApplication findById(int id); 
	public UserAccountApplication findByUserAccountId(int id); 
	
}
