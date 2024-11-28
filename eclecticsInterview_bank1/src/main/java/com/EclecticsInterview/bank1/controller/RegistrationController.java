package com.EclecticsInterview.bank1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.EclecticsInterview.bank1.model.LoginForm;
import com.EclecticsInterview.bank1.model.MyUser;
import com.EclecticsInterview.bank1.repository.MyUserRepository;
import com.EclecticsInterview.bank1.service.MyUserDetailService;
import com.EclecticsInterview.bank1.service.jwt.JwtService;

@RestController
public class RegistrationController {
	
	@Autowired
	private MyUserRepository myUserRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MyUserDetailService myUserDetailService;
	
	
	@PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginForm.username(), loginForm.password()
        ));
        if (authentication.isAuthenticated()) {
        	String test = jwtService.generateToken(myUserDetailService.loadUserByUsername(loginForm.username()));
        	System.out.println("test: "+test);
            return jwtService.generateToken(myUserDetailService.loadUserByUsername(loginForm.username()));
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }	

	
	@PostMapping("/user/reg/")
//	public MyUser createUser(@RequestBody MyUser myUser)
	public String createUser(@RequestBody MyUser myUser)
	{
		myUser.setPassword( passwordEncoder.encode( myUser.getPassword() ) ) ;
		if ( myUserRepository.findByUsername(myUser.getUsername()).isEmpty() )
		{
			return myUserRepository.save(myUser).toString();
		}
		else
		{
			return "Error: username:'"+ myUser.getUsername()  +"' already exists.";
		}
	}
	


	
}
