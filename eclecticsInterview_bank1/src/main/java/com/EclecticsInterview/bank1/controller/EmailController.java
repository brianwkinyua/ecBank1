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
import com.EclecticsInterview.bank1.model.PersonEmail;
import com.EclecticsInterview.bank1.repository.PersonEmailRepo;
import com.EclecticsInterview.bank1.repository.PersonRepo;


//@RestController
@Controller
public class EmailController {
	
//	@Autowired
	private PersonRepo myPersonRepo;
	private PersonEmailRepo myPersonEmailRepo;

	public EmailController(PersonRepo myPersonRepo, PersonEmailRepo myPersonEmailRepo) {
		super();
		this.myPersonRepo = myPersonRepo;
		this.myPersonEmailRepo = myPersonEmailRepo;
	}
	
    public static boolean useRegex(final String input) {
        // Compile regular expression
        final Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", Pattern.CASE_INSENSITIVE);
        // Match regex against input
        final Matcher matcher = pattern.matcher(input);
        // Use results...
        return matcher.matches();
    }
    
	@RequestMapping(
			  path = "/persons/{personId}/emails/new/", 
			  method = RequestMethod.POST,
			  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
			  produces = {
			    MediaType.APPLICATION_ATOM_XML_VALUE, 
			    MediaType.APPLICATION_JSON_VALUE,
			    MediaType.TEXT_HTML_VALUE
			  })
//	@PostMapping("/persons/new/")
//	@ResponseBody
	public String addPersonEmail(@PathVariable(name="personId") int personId, Model model, PersonEmail newPersonEmail, BindingResult bindingResult)
	{
		model.addAttribute("body_class", "admin");
		model.addAttribute("myTitle", "Add Person Email");
		model.addAttribute("headNfoot", true);
		
		Person myPerson = this.myPersonRepo.findById(personId) ;
		model.addAttribute("myPerson", myPerson );
		
		System.out.println("bindingResult: "+bindingResult);
		
		if (bindingResult.hasErrors())
		{
			System.out.println("bindingResult.hasErrors(): "+bindingResult.hasErrors());
			System.out.println("bindingResult.getAllErrors(): "+bindingResult.getAllErrors());
			System.out.println("addPersonEmail newPersonEmail: "+newPersonEmail);
			return bindingResult.getAllErrors().toString();
		}
		else
		{
			System.out.println("bindingResult.hasErrors(): "+bindingResult.hasErrors());
		}
		
		System.out.println("newPersonEmail: "+newPersonEmail );
		
		if(! useRegex( newPersonEmail.getEmailAddress() ) )
		{
//			model.addAttribute("content", "<h3>Error</h3><p>Invalid date of birth<br/><br/>Please try again.</p>");
			model.addAttribute("myTitle", "Error While Adding New Person Email");
			model.addAttribute("content", "Error\n\nInvalid email address.\n\n\n\nPlease try again.");
			return "personHome";
		}			

		this.myPersonEmailRepo.save(newPersonEmail);
		model.addAttribute("myTitle", "Successful Adding of Person Email");
		model.addAttribute("content", "Congrats\n\nYou have succesfully added a new email:\n\n"+newPersonEmail.toString()+"\n\nto person: "+myPerson.toString()+"\n\n...");
		return "personHome";
	}
	
	
	@GetMapping("/persons/{personId}/emails/new/")
	public String personPhoneNew(@PathVariable(name="personId") int personId,Model model)
	{
		model.addAttribute("body_class", "admin");
		model.addAttribute("myTitle", "Add New Email to Person");
		model.addAttribute("headNfoot", true);
		
		Person myPerson = this.myPersonRepo.findById(personId) ;
		model.addAttribute("myPerson", myPerson );
		
		return "register_person_email";
	}
	
}
