package com.EclecticsInterview.bank1.service;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		ArrayList<String> roles = new ArrayList<String>();
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch( grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN") );
		
		System.out.println("authentication.getAuthorities(): " + authentication.getAuthorities());
		
		authentication.getAuthorities()
			.stream()
			.forEach( grantedAuthority -> roles.add( grantedAuthority.getAuthority() ) ) ;
		
		System.out.println("roles: "+roles);
		/*
		if (isAdmin)
		{
			setDefaultTargetUrl( "/admin/" );
		}
		else
		{
			setDefaultTargetUrl( "/user/" );
		}
		*/
		if ( roles.contains("ROLE_ADMIN") )
		{
			System.out.println("roles.contains(\"ROLE_ADMIN\") ");
			setDefaultTargetUrl( "/admin/" );
		}
		else
		if ( roles.contains("ROLE_USER") )
		{
			System.out.println("roles.contains(\"ROLE_USER\") ");
			setDefaultTargetUrl( "/user/" );
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
}
