package com.EclecticsInterview.bank1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.EclecticsInterview.bank1.model.Deposit;
import com.EclecticsInterview.bank1.model.UserAccount;
import com.EclecticsInterview.bank1.model.Withdrawal;

@Repository
public interface DepositRepo extends CrudRepository<Deposit, Integer>{

	public List<Deposit> findAll(); 
//	public List<Deposit> findByUserAccount(UserAccount userAccount); 
	public List<Deposit> findByUserAccountId(int userAccountId); 
	public Deposit findById(int id); 
	
}
