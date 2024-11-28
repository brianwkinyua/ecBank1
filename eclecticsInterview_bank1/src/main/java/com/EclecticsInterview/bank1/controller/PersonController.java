package com.EclecticsInterview.bank1.controller;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.EclecticsInterview.bank1.model.Person;
import com.EclecticsInterview.bank1.repository.DependantRepo;
import com.EclecticsInterview.bank1.repository.PersonEmailRepo;
import com.EclecticsInterview.bank1.repository.PersonPhoneRepo;
import com.EclecticsInterview.bank1.repository.PersonRepo;
import com.EclecticsInterview.bank1.repository.UserAccountApplicationRepo;



//@RestController
@Controller
public class PersonController {
	
//	@Autowired
	private PersonRepo myPersonRepo;
	private PersonPhoneRepo myPersonPhoneRepo;
	private PersonEmailRepo myPersonEmailRepo;
	private DependantRepo myDependantRepo;
	private UserAccountApplicationRepo myUserAccountApplicationRepo;

	public PersonController(PersonRepo myPersonRepo, PersonPhoneRepo myPersonPhoneRepo,
			PersonEmailRepo myPersonEmailRepo, DependantRepo myDependantRepo,
			UserAccountApplicationRepo myUserAccountApplicationRepo) {
		super();
		this.myPersonRepo = myPersonRepo;
		this.myPersonPhoneRepo = myPersonPhoneRepo;
		this.myPersonEmailRepo = myPersonEmailRepo;
		this.myDependantRepo = myDependantRepo;
		this.myUserAccountApplicationRepo = myUserAccountApplicationRepo;
	}

    public static boolean useRegex(final String input) {
        // Compile regular expression
        final Pattern pattern = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d", Pattern.CASE_INSENSITIVE);
        // Match regex against input
        final Matcher matcher = pattern.matcher(input);
        // Use results...
        return matcher.matches();
    }
    
    public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }    
	@GetMapping("/persons/")
	public String showPersons(Model model)
	{
		model.addAttribute("body_class", "admin");
		model.addAttribute("myTitle", "Persons List");
		model.addAttribute("headNfoot", true);
		
		model.addAttribute("myUserName", SecurityContextHolder.getContext().getAuthentication().getName()  );
		model.addAttribute("myUserRole", SecurityContextHolder.getContext().getAuthentication().getAuthorities()  );
		
		model.addAttribute("persons", this.myPersonRepo.findAll() );
		return "persons_list.html";
		
	}
	
	@GetMapping("/persons/api/")
	@ResponseBody
	public List<Person> showPersonsAPI()
	{
		List<Person> persons = new ArrayList<Person>();

		this.myPersonRepo
		.findAll()
		.forEach(persons::add);
		
		return persons;
	}
	
	@GetMapping("/persons/api/{pid}/")
	@ResponseBody
	public Person showOnePersonAPI(@PathVariable(name="pid") int pid)
	{
		System.out.println("showOnePerson | pid: "+pid);
		return this.myPersonRepo.findById(pid);
	}
	
	@GetMapping("/persons/{pid}/")
	public String showOnePerson(@PathVariable(name="pid") int pid, Model model)
	{
		model.addAttribute("body_class", "admin");
		System.out.println("showOnePerson | pid: "+pid);
		model.addAttribute("myTitle", "Person Details");
		model.addAttribute("headNfoot", true);
		
		model.addAttribute("myUserName", SecurityContextHolder.getContext().getAuthentication().getName()  );
		model.addAttribute("myUserRole", SecurityContextHolder.getContext().getAuthentication().getAuthorities()  );
		
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
		
		model.addAttribute("person", this.myPersonRepo.findById(pid) );
		
		model.addAttribute("personPhones", this.myPersonPhoneRepo.findByPersonId(pid) );
		
		model.addAttribute("personEmails", this.myPersonEmailRepo.findByPersonId(pid) );
		
		model.addAttribute("personDependants", this.myDependantRepo.findByPersonId(pid) );
		
		model.addAttribute("personUserAccountApplications", this.myUserAccountApplicationRepo.findByPersonId(pid) );
		
		return "showOnePerson";
	}
	
	@RequestMapping(
			  path = "/persons/new/", 
			  method = RequestMethod.POST,
			  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
			  produces = {
			    MediaType.APPLICATION_ATOM_XML_VALUE, 
			    MediaType.APPLICATION_JSON_VALUE,
			    MediaType.TEXT_HTML_VALUE
			  })
//	@PostMapping("/persons/new/")
//	@ResponseBody
	public String addPerson(Model model, Person newPerson, BindingResult bindingResult)
	{
		model.addAttribute("body_class", "admin");
		model.addAttribute("myTitle", "Add Person");
		model.addAttribute("headNfoot", true);
		
		
		model.addAttribute("myUserName", SecurityContextHolder.getContext().getAuthentication().getName()  );
		model.addAttribute("myUserRole", SecurityContextHolder.getContext().getAuthentication().getAuthorities()  );
		
		int temp_age = -1;
		System.out.println("bindingResult: "+bindingResult);
		
		if (bindingResult.hasErrors())
		{
			System.out.println("bindingResult.hasErrors(): "+bindingResult.hasErrors());
			System.out.println("bindingResult.getAllErrors(): "+bindingResult.getAllErrors());
			System.out.println("addPerson newPerson: "+newPerson);
			return bindingResult.getAllErrors().toString();
		}
		else
		{
			System.out.println("bindingResult.hasErrors(): "+bindingResult.hasErrors());
		}
		
		System.out.println("newPerson: "+newPerson );
		System.out.println("newPerson.getDateOfBirth(): "+newPerson.getDateOfBirth());
		
		System.out.println( "useRegex( newPerson.getDateOfBirth() ) : " + useRegex( newPerson.getDateOfBirth() )  );
		
		if( useRegex( newPerson.getDateOfBirth() ) )
		{
			LocalDate currentDate = LocalDate.now(); 
			System.out.println("currentDate: "+currentDate);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
			
			LocalDate localDateOfBirth;
			try {
				localDateOfBirth = LocalDate.parse( newPerson.getDateOfBirth(), formatter );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				model.addAttribute("myTitle", "Error While Adding Person");
				model.addAttribute("content", "Error\n\nInvalid date of birth.\n\nError:"+e.getMessage()+"\n\nPlease try again.");
				return "home";
			}
			
			System.out.println("localDateOfBirth: "+localDateOfBirth); // 2010-01-02
			
			temp_age = calculateAge(localDateOfBirth, currentDate) ;
			System.out.println("temp_age: "+ temp_age);
		}
		else
		{
//			model.addAttribute("content", "<h3>Error</h3><p>Invalid date of birth<br/><br/>Please try again.</p>");
			model.addAttribute("myTitle", "Error While Adding Person");
			model.addAttribute("content", "Error\n\nInvalid date of birth.\n\nPlease try again.");
			return "home";
		}
			
			
//		if(true)
		if (temp_age<18 && newPerson.getMaritalStatus().toLowerCase().contains("married"))
		{
			/*
			model.addAttribute("content", "<h3>Error</h3><p>The person's age does not allow them to marry."
					+"<br/><br/>Please try again.</p>"
					+ "<p><br/><br/><a href=\"/admin/\">Admin Home</a>"
					+"<br/><br/><a href=\"/user/\">User Home</a>"
					+"<br/><br/><a href=\"/\">Guest Home</a>");
			*/
			model.addAttribute("myTitle", "Error While Adding Person");
			model.addAttribute("content", "Error\n\nThe person's age does not allow them to marry.\n\nPlease try again.");

			/*
			return "<h3>Error</h3><p>The person's age does not allow them to marry."
					+"<br/><br/>Please try again.</p>"
					+ "<p><br/><br/><a href=\"/admin/\">Admin Home</a>"
					+"<br/><br/><a href=\"/user/\">User Home</a>"
					+"<br/><br/><a href=\"/\">Guest Home</a>";
//			return "Error\n\nThe person's age does not allow them to marry.\n\nPlease try again.</p>";
			 * 
			 */
			return "home";
		}
		else
		{
			System.out.println("addPerson newPerson: "+newPerson);
			this.myPersonRepo.save(newPerson);
			/*
			model.addAttribute("content", "<h3>Congrats</h3>You have succesfully added:<br/><br/>"+newPerson.toString()
			+ "<p><br/><br/><a href=\"/admin/\">Admin Home</a>"
			+"<br/><br/><a href=\"/user/\">User Home</a>"
			+"<br/><br/><a href=\"/\">Guest Home</a>");
			*/
			model.addAttribute("myTitle", "Successful Adding of Person");
			model.addAttribute("content", "Congrats\n\nYou have succesfully added:\n\n"+newPerson.toString()+"\n\n...");
			
//			return "Congrats\n\nYou have succesfully added:\n\n"+newEPerson.toString();
			
		}
		
		
//		return "register_person_results";
		return "home";

	}
	
	@GetMapping("/persons/new/")
	public String personNew(Model model)
	{
		model.addAttribute("body_class", "admin");
		model.addAttribute("headNfoot", true);
		
		model.addAttribute("myUserName", SecurityContextHolder.getContext().getAuthentication().getName()  );
		model.addAttribute("myUserRole", SecurityContextHolder.getContext().getAuthentication().getAuthorities()  );
		
		return "register_person";
	}
	

	/*
	@PutMapping("/persons/updt/")
	@ResponseBody
	public String updateCourse(@RequestBody Person updtPerson, BindingResult bindingResult)
	{
		System.out.println("bindingResult: "+bindingResult);
		
		if (bindingResult.hasErrors())
		{
			System.out.println("bindingResult.getAllErrors(): "+bindingResult.getAllErrors());
			System.out.println("updatePerson | updtPerson: "+updtPerson);
			return "";
		}
		
		System.out.println("updateEPerson | updtEPerson: "+updtPerson);
		this.myPersonRepo.save(updtPerson);
		return "";
	}

	@DeleteMapping("/persons/del/{pid}/")
	@ResponseBody
	public int delOnePerson(@PathVariable(name="pid") Integer id)
	{
		System.out.println("delOneEPerson | id: "+id);
		try {
			this.myPersonRepo.deleteById(id);
			return 1;
		} catch (Exception e) {
			System.out.println("Exception e: "+e);
			return -1;
		}
	}
	*/

	
	
}
