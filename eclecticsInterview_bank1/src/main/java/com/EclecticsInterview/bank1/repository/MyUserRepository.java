package com.EclecticsInterview.bank1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EclecticsInterview.bank1.model.MyUser;

public interface MyUserRepository extends JpaRepository<MyUser, Long>{
	Optional<MyUser> findByUsername(String username);
}
