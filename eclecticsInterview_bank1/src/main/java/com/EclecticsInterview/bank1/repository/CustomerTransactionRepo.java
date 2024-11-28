package com.EclecticsInterview.bank1.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.EclecticsInterview.bank1.model.CustomerTransaction;

@Repository
public interface CustomerTransactionRepo extends CrudRepository<CustomerTransaction, Integer>{

	public List<CustomerTransaction> findAll(); 
	public List<CustomerTransaction> findByCustomerId(int customerId);
	public CustomerTransaction findById(int id); 
	
}
