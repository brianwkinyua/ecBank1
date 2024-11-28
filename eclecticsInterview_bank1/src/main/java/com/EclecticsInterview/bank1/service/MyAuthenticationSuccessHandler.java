package com.EclecticsInterview.bank1.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.EclecticsInterview.bank1.model.MyUser;
import com.EclecticsInterview.bank1.repository.MyUserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
//public class MyAuthenticationSuccessHandler extends MySimpleUrlAuthenticationSuccessHandler{
	
    protected Log logger = LogFactory.getLog(this.getClass());

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    
    @Autowired
    private MyUserRepository myUserRepository ; 
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		/*
		 * 
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch( grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN") );
				
		if (isAdmin)
		{
			setDefaultTargetUrl( "/admin/" );
		}
		else
		{
			setDefaultTargetUrl( "/user/" );
		}
		
		*/

		
		ArrayList<String> roles = new ArrayList<String>();
		System.out.println("authentication.getAuthorities(): " + authentication.getAuthorities());
		authentication.getAuthorities()
			.stream()
			.forEach( grantedAuthority -> roles.add( grantedAuthority.getAuthority() ) ) ;
		System.out.println("roles: "+roles);

		if ( roles.toString().contains("ROLE_ADMIN") )
		{
			System.out.println("roles.contains(\"ROLE_ADMIN\") ");
			setDefaultTargetUrl( "/admin/" );
		}
		else
		if ( roles.toString().contains("ROLE_USER") )
		{
			System.out.println("roles.contains(\"ROLE_USER\") ");
			setDefaultTargetUrl( "/user/" );
		}
		else
		if ( roles.toString().contains("ROLE_CUSTOMER") )
		{
			System.out.println("roles.contains(\"ROLE_CUSTOMER\") ");
			setDefaultTargetUrl( "/customer/" );
		}
		else
		{
			System.out.println("Blah!");
		}
		
//		super.onAuthenticationSuccess(request, response, authentication);
		handle(request, response, authentication);
		clearAuthenticationAttributes(request);
	}
	
	protected void handle(
	        HttpServletRequest request,
	        HttpServletResponse response, 
	        Authentication authentication
	) throws IOException {

//	    String targetUrl = determineTargetUrl(authentication);
	    String targetUrl = determineTargetUrl2(authentication);

	    if (response.isCommitted()) {
	        logger.debug(
	                "Response has already been committed. Unable to redirect to "
	                        + targetUrl);
	        return;
	    }

	    redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	protected String determineTargetUrl(final Authentication authentication) {

	    Map<String, String> roleTargetUrlMap = new HashMap<>();
	    roleTargetUrlMap.put("ROLE_USER", "/homepage.html");
	    roleTargetUrlMap.put("ROLE_ADMIN", "/console.html");

	    final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	    for (final GrantedAuthority grantedAuthority : authorities) {
	        String authorityName = grantedAuthority.getAuthority();
	        if(roleTargetUrlMap.containsKey(authorityName)) {
	            return roleTargetUrlMap.get(authorityName);
	        }
	    }

	    throw new IllegalStateException();
	}
	
	protected String determineTargetUrl2(final Authentication authentication) {
		String targetUrl = "/";
		ArrayList<String> roles = new ArrayList<String>();
		System.out.println("authentication.getAuthorities(): " + authentication.getAuthorities());
		authentication.getAuthorities()
			.stream()
			.forEach( grantedAuthority -> roles.add( grantedAuthority.getAuthority() ) ) ;
		System.out.println("roles: "+roles);
		
		if ( roles.toString().contains("ROLE_ADMIN") )
		{
			System.out.println("roles.contains(\"ROLE_ADMIN\") ");
			targetUrl="/admin/";
		}
		else
		if ( roles.toString().contains("ROLE_USER") )
		{
			System.out.println("roles.contains(\"ROLE_USER\") ");
			targetUrl="/user/";
		}
		else
		if ( roles.toString().contains("ROLE_CUSTOMER") )
		{
			String currUserName = authentication.getName();
			System.out.println("debug: b4");
			MyUser currUserObj = myUserRepository.findByUsername(currUserName).get();
			
			System.out.println("debug: after");
			System.out.println("currUserObj: "+currUserObj);
			
			int uid = currUserObj.getId().intValue();
//			int uid = 2;
			System.out.println("roles.contains(\"ROLE_CUSTOMER\") ");
			targetUrl="/customers/"+uid+"/";
		}
		
		return targetUrl;
	}

	/*
	protected void clearAuthenticationAttributes(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    if (session == null) {
	        return;
	    }
	    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
	*/
	
}
