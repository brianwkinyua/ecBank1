package com.EclecticsInterview.bank1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.EclecticsInterview.bank1.model.UserAccount;


@Repository
public interface UserAccountRepo extends CrudRepository<UserAccount, Integer>{

	public List<UserAccount> findAll(); 
//	public List<UserAccount> findByPerson(Person person); 
	public List<UserAccount> findByPersonId(int personId); 
	public UserAccount findById(int id); 
	
}
