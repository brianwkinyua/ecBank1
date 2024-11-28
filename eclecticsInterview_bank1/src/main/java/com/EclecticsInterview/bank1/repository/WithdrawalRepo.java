package com.EclecticsInterview.bank1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.EclecticsInterview.bank1.model.UserAccount;
import com.EclecticsInterview.bank1.model.Withdrawal;

@Repository
public interface WithdrawalRepo extends CrudRepository<Withdrawal, Integer>{

	public List<Withdrawal> findAll(); 
//	public List<Withdrawal> findByUserAccount(UserAccount userAccount); 
	public List<Withdrawal> findByUserAccountId(int userAccountId); 
	public Withdrawal findById(int id); 
	
}
