package com.EclecticsInterview.bank1.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//@RestController
public class ContentController {
	
    
	@GetMapping("/")
//	@ResponseBody // returns plain text
	public String welcome(Model model)
	{
		model.addAttribute("myTitle", "Guest Home");
		model.addAttribute("body_class", "home");
		model.addAttribute("content", "Home stuff");
		
		model.addAttribute("headNfoot", true);
		
		model.addAttribute("myUserName", SecurityContextHolder.getContext().getAuthentication().getName()  );
		model.addAttribute("myUserRole", SecurityContextHolder.getContext().getAuthentication().getAuthorities()  );
		
		/*
		System.out.println("SecurityContextHolder.getContext().getAuthentication().getAuthorities().getClass() "+SecurityContextHolder.getContext().getAuthentication().getAuthorities().getClass() );
		System.out.println("SecurityContextHolder.getContext().getAuthentication().getAuthorities() "+SecurityContextHolder.getContext().getAuthentication().getAuthorities() );
		
		for (GrantedAuthority iterable_element : SecurityContextHolder.getContext().getAuthentication().getAuthorities() ) {
			System.out.println("iterable_element: "+iterable_element);
			System.out.println(iterable_element.toString());
			System.out.println(iterable_element.getAuthority());
		}
		*/
		boolean isAcc = false;
		if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains("ACCOUNTS") )
		{
			System.out.println("\nACCOUNTS is there.\n");
			isAcc=true;
		}
		else
		{
			System.out.println("\nNO ACCOUNTS\n");
			isAcc=false;
		}
		
		model.addAttribute("isAcc", isAcc);
		
		System.out.println("\n/home\n");
		return "home";
	}
	
	@GetMapping("/user/")
	public String user(Model model)
	{
		model.addAttribute("myTitle", "User Home");
		model.addAttribute("body_class", "user");
		model.addAttribute("content", "User stuff");
		model.addAttribute("headNfoot", true);
		
		model.addAttribute("myUserName", SecurityContextHolder.getContext().getAuthentication().getName()  );
		model.addAttribute("myUserRole", SecurityContextHolder.getContext().getAuthentication().getAuthorities()  );
		
		System.out.println("\n/user\n");
		return "home";
	}
	
	/*
	 * @GetMapping("/user/reg/") public String userReg() { return
	 * "<h1>User Reg GET!</h1>"; }
	 */	
	
	@GetMapping("/admin/")
	public String admin(Model model)
	{
		model.addAttribute("myTitle", "Admin Home");
		model.addAttribute("body_class", "admin");
		model.addAttribute("content", "Admin stuff");
		model.addAttribute("headNfoot", true);
		
		model.addAttribute("myUserName", SecurityContextHolder.getContext().getAuthentication().getName()  );
		model.addAttribute("myUserRole", SecurityContextHolder.getContext().getAuthentication().getAuthorities()  );
		
		System.out.println("\n/admin\n");
		return "home";
	}
	
	@GetMapping("/login")
	public String login(Model model)
	{
		model.addAttribute("body_class", "home");
		model.addAttribute("headNfoot", false);
		return "custom_login";
	}
	
	@GetMapping("/logout")
	public String logout(Model model)
	{
		model.addAttribute("body_class", "home");
		model.addAttribute("headNfoot", false);
		return "custom_logout";
	}
	
}
