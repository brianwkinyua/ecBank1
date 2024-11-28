package com.EclecticsInterview.bank1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.EclecticsInterview.bank1.model.MyOTP;

@Repository
public interface MyOTPRepo extends CrudRepository<MyOTP, Integer>{

	public List<MyOTP> findAll(); 
	public List<MyOTP> findByUserAccountId(int userAccountId); 
	public MyOTP findById(int id); 
	
}
