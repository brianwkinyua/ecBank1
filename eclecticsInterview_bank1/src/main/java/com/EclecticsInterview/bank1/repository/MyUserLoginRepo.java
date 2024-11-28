package com.EclecticsInterview.bank1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.EclecticsInterview.bank1.model.MyUserLogin;

@Repository
public interface MyUserLoginRepo extends CrudRepository<MyUserLogin, Integer>{

	public List<MyUserLogin> findAll(); 
	public List<MyUserLogin> findByUserAccountId(int userAccountId); 
	public MyUserLogin findById(int id); 
	
}
