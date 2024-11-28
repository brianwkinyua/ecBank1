package com.EclecticsInterview.bank1.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.EclecticsInterview.bank1.model.MyUser;
import com.EclecticsInterview.bank1.repository.MyUserRepository;

@Service
public class MyUserDetailService implements UserDetailsService{
	
	@Autowired
	private MyUserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<MyUser> user = repository.findByUsername(username);
		if (user.isPresent())
		{
			var userObj = user.get();
			return User.builder()
					.username(userObj.getUsername())
					.password(userObj.getPassword())
					.roles(getRoles(userObj))
					.build();
		}
		else
		{
			throw new UsernameNotFoundException(username+" not found.");
		}
	}

	private String [] getRoles(MyUser userObj) {
		if (userObj.getRole()==null)
		{
			return new String [] {"GUEST"};
		}
		return userObj.getRole().split(",");
	}

}
