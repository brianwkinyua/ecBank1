package com.EclecticsInterview.bank1.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.EclecticsInterview.bank1.model.Person;
import com.EclecticsInterview.bank1.model.PersonPhone;
import com.EclecticsInterview.bank1.repository.PersonPhoneRepo;
import com.EclecticsInterview.bank1.repository.PersonRepo;


//@RestController
@Controller
public class PhoneController {
	
//	@Autowired
	private PersonRepo myPersonRepo;
	private PersonPhoneRepo myPersonPhoneRepo;

	public PhoneController(PersonRepo myPersonRepo, PersonPhoneRepo myPersonPhoneRepo) {
		super();
		this.myPersonRepo = myPersonRepo;
		this.myPersonPhoneRepo = myPersonPhoneRepo;
	}
	
    public static boolean useRegex(final String input) {
        // Compile regular expression
        final Pattern pattern = Pattern.compile("^\\d{9,13}$", Pattern.CASE_INSENSITIVE);
        // Match regex against input
        final Matcher matcher = pattern.matcher(input);
        // Use results...
        return matcher.matches();
    }
    

	
	@GetMapping("/phones/{phoneId}/")
	public String showOnePhone(@PathVariable(name="phoneId") int phoneId, Model model)
	{
		model.addAttribute("body_class", "admin");
		System.out.println("showOnePhone | phoneId: "+phoneId);
		model.addAttribute("myTitle", "Phone Details");
		model.addAttribute("headNfoot", true);
		
		PersonPhone myPersonPhone = this.myPersonPhoneRepo.findById(phoneId) ;
		Person myPerson = this.myPersonRepo.findById(myPersonPhone.getPersonId()) ;
		
		model.addAttribute("myPersonPhone", myPersonPhone );
		model.addAttribute("myPerson", myPerson );
		
		return "showOnePhone";
	}
	
	@RequestMapping(
			  path = "/persons/{personId}/phones/new/", 
			  method = RequestMethod.POST,
			  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
			  produces = {
			    MediaType.APPLICATION_ATOM_XML_VALUE, 
			    MediaType.APPLICATION_JSON_VALUE,
			    MediaType.TEXT_HTML_VALUE
			  })
//	@PostMapping("/persons/new/")
//	@ResponseBody
	public String addPersonPhone(@PathVariable(name="personId") int personId, Model model, PersonPhone newPersonPhone, BindingResult bindingResult)
	{
		model.addAttribute("body_class", "admin");
		model.addAttribute("myTitle", "Add Person Phone");
		model.addAttribute("headNfoot", true);
		
		Person myPerson = this.myPersonRepo.findById(personId) ;
		model.addAttribute("myPerson", myPerson );
		
		System.out.println("bindingResult: "+bindingResult);
		
		if (bindingResult.hasErrors())
		{
			System.out.println("bindingResult.hasErrors(): "+bindingResult.hasErrors());
			System.out.println("bindingResult.getAllErrors(): "+bindingResult.getAllErrors());
			System.out.println("addPersonPhone newPersonPhone: "+newPersonPhone);
			return bindingResult.getAllErrors().toString();
		}
		else
		{
			System.out.println("bindingResult.hasErrors(): "+bindingResult.hasErrors());
		}
		
		System.out.println("newPersonPhone: "+newPersonPhone );
		
		if(! useRegex( newPersonPhone.getNumber() ) )
		{
//			model.addAttribute("content", "<h3>Error</h3><p>Invalid date of birth<br/><br/>Please try again.</p>");
			model.addAttribute("myTitle", "Error While Adding New Person Phone");
			model.addAttribute("content", "Error\n\nInvalid phone number.\n\nNumber must have between 9 and 13 digits.\n\nPlease try again.");
			return "personHome";
		}			

		this.myPersonPhoneRepo.save(newPersonPhone);
		model.addAttribute("myTitle", "Successful Adding of Person Phone");
		model.addAttribute("content", "Congrats\n\nYou have succesfully added a new phone:\n\n"+newPersonPhone.toString()+"\n\nto person: " + myPerson.toString()+"\n\n...");
		return "personHome";
	}
	
	
	@GetMapping("/persons/{personId}/phones/new/")
	public String personPhoneNew(@PathVariable(name="personId") int personId,Model model)
	{
		model.addAttribute("body_class", "admin");
		model.addAttribute("myTitle", "Add New Phone to Person");
		model.addAttribute("headNfoot", true);
		
		Person myPerson = this.myPersonRepo.findById(personId) ;
		model.addAttribute("myPerson", myPerson );
		
		return "register_person_phone";
	}
	
}
