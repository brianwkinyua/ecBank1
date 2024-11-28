package com.EclecticsInterview.bank1.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.EclecticsInterview.bank1.model.MyOTP;
import com.EclecticsInterview.bank1.model.MyUser;
import com.EclecticsInterview.bank1.model.Person;
import com.EclecticsInterview.bank1.model.UserAccountApplication;
import com.EclecticsInterview.bank1.model.UserAccountApplicationForm;
import com.EclecticsInterview.bank1.repository.MyOTPRepo;
import com.EclecticsInterview.bank1.repository.MyUserRepository;
import com.EclecticsInterview.bank1.repository.PersonRepo;
import com.EclecticsInterview.bank1.repository.UserAccountApplicationRepo;
import com.EclecticsInterview.bank1.service.MyUtils1;



//@RestController
@Controller
public class UserAccountApplicationController {
	
	private PersonRepo myPersonRepo;
	private UserAccountApplicationRepo myUserAccountApplicationRepo;
	private MyUserRepository myUserRepository;
	private PasswordEncoder passwordEncoder;
	private MyOTPRepo myOTPRepo;

	public UserAccountApplicationController(
			PersonRepo myPersonRepo,
			UserAccountApplicationRepo myUserAccountApplicationRepo,
			MyUserRepository myUserRepository,
			PasswordEncoder passwordEncoder,
			MyOTPRepo myOTPRepo
			) {
		super();
		this.myPersonRepo = myPersonRepo;
		this.myUserAccountApplicationRepo = myUserAccountApplicationRepo;
		this.myUserRepository = myUserRepository;
		this.passwordEncoder = passwordEncoder;
		this.myOTPRepo = myOTPRepo;
	}

    public static boolean dateRegex(final String input) {
        // Compile regular expression
        final Pattern pattern = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d", Pattern.CASE_INSENSITIVE);
        // Match regex against input
        final Matcher matcher = pattern.matcher(input);
        // Use results...
        return matcher.matches();
    }
    

    public static boolean usernameRegex(final String input) {
        // Compile regular expression
        final Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9._-]{2,15}$", Pattern.CASE_INSENSITIVE);
        // Match regex against input
        final Matcher matcher = pattern.matcher(input);
        // Use results...
        return matcher.matches();
    }
    

	
	@RequestMapping(
			  path = "/persons/{personId}/userAccountApplications/new/", 
			  method = RequestMethod.POST,
			  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
			  produces = {
			    MediaType.APPLICATION_ATOM_XML_VALUE, 
			    MediaType.APPLICATION_JSON_VALUE,
			    MediaType.TEXT_HTML_VALUE
			  })
//	@PostMapping("/persons/new/")
//	@ResponseBody
	public String addPersonUserAccountApplication(@PathVariable(name="personId") int personId, Model model, UserAccountApplication newUserAccountApplication, BindingResult bindingResult)
	{
		model.addAttribute("body_class", "admin");
		model.addAttribute("myTitle", "Add Person User Account Application");
		model.addAttribute("headNfoot", true);
		
		Person myPerson = this.myPersonRepo.findById(personId) ;
		model.addAttribute("myPerson", myPerson );
		
		System.out.println("bindingResult: "+bindingResult);
		
		if (bindingResult.hasErrors())
		{
			System.out.println("bindingResult.hasErrors(): "+bindingResult.hasErrors());
			System.out.println("bindingResult.getAllErrors(): "+bindingResult.getAllErrors());
			System.out.println("addPersonUserAccountApplication newUserAccountApplication: "+newUserAccountApplication);
			return bindingResult.getAllErrors().toString();
		}
		else
		{
			System.out.println("bindingResult.has-NO-Errors(): "+bindingResult.hasErrors());
		}
		
		System.out.println("newUserAccountApplication: "+newUserAccountApplication );
		
		
		if( dateRegex( newUserAccountApplication.getDate() ) )
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
			
			LocalDate localDateOfApplication;
			try {
				localDateOfApplication = LocalDate.parse( newUserAccountApplication.getDate(), formatter );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				model.addAttribute("myTitle", "Error While Adding New User Account Application");
				model.addAttribute("content", "Error\n\nInvalid date of Application.\n\nError:"+e.getMessage()+"\n\nPlease try again.");
				return "personHome";
			}
			
			System.out.println("localDateOfBirth: "+localDateOfApplication); // 2010-01-02
			
		}
		else
		{
//			model.addAttribute("content", "<h3>Error</h3><p>Invalid date of birth<br/><br/>Please try again.</p>");
			model.addAttribute("myTitle", "Error While Adding New User Account Application");
			model.addAttribute("content", "Error\n\nInvalid date of Application.\n\nPlease try again.");
			return "personHome";
		}
		
		
		if( ! usernameRegex( newUserAccountApplication.getTargetUsername() ) )
		{
//			model.addAttribute("content", "<h3>Error</h3><p>Invalid date of birth<br/><br/>Please try again.</p>");
			model.addAttribute("myTitle", "Error While Adding New Person User Account Application");
			model.addAttribute("content", "Error\n\nInvalid username.\n\n username starts with a letter (uppercase or lowercase).\n has digits, dots (.), underscores (_), and hyphens (-).\n Ensures the total length is between 3 and 16 characters. \n No spaces or special characters (e.g., @, #, etc.) are allowed.\n\nPlease try again.");
			return "personHome";
		}

		
		this.myUserAccountApplicationRepo.save(newUserAccountApplication);
		model.addAttribute("myTitle", "Successful Adding of Person User Account Application");
		model.addAttribute("content", "Congrats\n\nYou have succesfully added a new User Account Application:\n\n"+newUserAccountApplication.toString()+"\n\nto person: " + myPerson.toString()+"\n\n...");
		return "personHome";
	}
	
	
	@GetMapping("/persons/{personId}/userAccountApplications/new/")
	public String personNewUserAccountApplication(@PathVariable(name="personId") int personId,Model model)
	{
		model.addAttribute("body_class", "admin");
		model.addAttribute("myTitle", "Add New User Account Application to Person");
		model.addAttribute("headNfoot", true);
		
		Person myPerson = this.myPersonRepo.findById(personId) ;
		model.addAttribute("myPerson", myPerson );
		
//		System.out.println("personDependantNew: ");
//		System.out.println("dependantOptions: "+dependantOptions);
		
		return "register_person_userAccountApplication";
	}
	
//	@PostMapping("/procApplication/reject/{uaaid}/")
	@RequestMapping(
			  path = "/procApplication/reject/{uaaID}/", 
			  method = RequestMethod.POST,
			  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
			  produces = {
			    MediaType.APPLICATION_ATOM_XML_VALUE, 
			    MediaType.APPLICATION_JSON_VALUE,
			    MediaType.TEXT_HTML_VALUE
			  })
///	@ResponseBody
	public String userAccountApplicationRejection(@PathVariable(name="uaaID") int uaaID, Model model, UserAccountApplicationForm userAccountApplicationForm, BindingResult bindingResult)
	{
		System.out.println("userAccountApplicationRejection userAccountApplicationForm: "+userAccountApplicationForm);
		UserAccountApplication uaa = this.myUserAccountApplicationRepo.findById(uaaID) ;
		uaa.setStatus("REJECTED");
		uaa.setUpdated(LocalDate.now().toString());
		this.myUserAccountApplicationRepo.save(uaa);
		return "redirect:/persons/"+uaa.getPersonId()+"/";
	}
	
	
//	@PostMapping("/procApplication/approve/{uaaid}/")
	@RequestMapping(
			  path = "/procApplication/approve/{uaaID}/", 
			  method = RequestMethod.POST,
			  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
			  produces = {
			    MediaType.APPLICATION_ATOM_XML_VALUE, 
			    MediaType.APPLICATION_JSON_VALUE,
			    MediaType.TEXT_HTML_VALUE
			  })
///	@ResponseBody
	public String userAccountApplicationApproval(@PathVariable(name="uaaID") int uaaID, Model model, UserAccountApplicationForm userAccountApplicationForm, BindingResult bindingResult)
	{
		System.out.println("userAccountApplicationApproval userAccountApplicationForm: "+userAccountApplicationForm);
		UserAccountApplication uaa = this.myUserAccountApplicationRepo.findById(uaaID) ;
		
		// create user account
		MyUser newUser = new MyUser();
		newUser.setUsername(uaa.getTargetUsername());
		String newPassword = MyUtils1.generatePassword(10);
		newUser.setPassword( passwordEncoder.encode(newPassword) );
		newUser.setRole("CUSTOMER");
		
		MyUser newUserResult = null;
		
		if ( myUserRepository.findByUsername(newUser.getUsername()).isEmpty() )
		{
			newUserResult = myUserRepository.save(newUser);
		}
		else
		{
//			model.addAttribute("content", "<h3>Error</h3><p>Invalid date of birth<br/><br/>Please try again.</p>");
			model.addAttribute("myTitle", "Error While creating a new customer account.");
			model.addAttribute("content", "Error\n\nusername:'"+ newUser.getUsername()  +"' already exists.\"\n\n\n\nPlease try again.");
			return "personHome";
		}

		
		MyOTP newUserOTP = new MyOTP();
		newUserOTP.setCreated( LocalDateTime.now() );
		
		// Get current time
        LocalDateTime now = LocalDateTime.now();
        // Add 24 hours to the current time
        LocalDateTime expiryTime = now.plusHours(24);
        // Display expiry time in LocalDateTime format
        System.out.println("Expiry Time (LocalDateTime): " + expiryTime);
		
		newUserOTP.setExpiry( expiryTime );
		newUserOTP.setUserAccountId( newUserResult.getId().intValue() );
		newUserOTP.setOtp(newPassword);
		newUserOTP.setLoginAttempt(-1);
		this.myOTPRepo.save(newUserOTP);
		
		uaa.setStatus("APPROVED");
		uaa.setUpdated(LocalDate.now().toString());
		uaa.setUserAccountId( newUserResult.getId().intValue() );
		
		this.myUserAccountApplicationRepo.save(uaa);
		
		return "redirect:/persons/"+uaa.getPersonId()+"/";
	}

	
	
}

