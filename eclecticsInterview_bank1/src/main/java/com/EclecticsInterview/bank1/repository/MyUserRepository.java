package com.EclecticsInterview.bank1.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.EclecticsInterview.bank1.model.MyUser;

@Repository
//public interface MyUserRepository extends JpaRepository<MyUser, Long>{
public interface MyUserRepository extends CrudRepository<MyUser, Long>{
	Optional<MyUser> findByUsername(String username);
	public MyUser findById(int id); 
}
