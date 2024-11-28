package com.EclecticsInterview.bank1.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.EclecticsInterview.bank1.model.Dependant;
import com.EclecticsInterview.bank1.model.Person;
import com.EclecticsInterview.bank1.repository.DependantRepo;
import com.EclecticsInterview.bank1.repository.PersonRepo;


//@RestController
@Controller
public class DependantController {
	
//	@Autowired
	private PersonRepo myPersonRepo;
	private DependantRepo myDependantRepo;

	public DependantController(PersonRepo myPersonRepo, DependantRepo myDependantRepo) {
		super();
		this.myPersonRepo = myPersonRepo;
		this.myDependantRepo = myDependantRepo;
	}
	
	@RequestMapping(
			  path = "/persons/{personId}/dependants/new/", 
			  method = RequestMethod.POST,
			  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
			  produces = {
			    MediaType.APPLICATION_ATOM_XML_VALUE, 
			    MediaType.APPLICATION_JSON_VALUE,
			    MediaType.TEXT_HTML_VALUE
			  })
//	@PostMapping("/persons/new/")
//	@ResponseBody
	public String addPersonDependant(@PathVariable(name="personId") int personId, Model model, Dependant newDependant, BindingResult bindingResult)
	{
		model.addAttribute("body_class", "admin");
		model.addAttribute("myTitle", "Add Person Dependant");
		model.addAttribute("headNfoot", true);
		
		Person myPerson = this.myPersonRepo.findById(personId) ;
		model.addAttribute("myPerson", myPerson );
		
		System.out.println("bindingResult: "+bindingResult);
		
		if (bindingResult.hasErrors())
		{
			System.out.println("bindingResult.hasErrors(): "+bindingResult.hasErrors());
			System.out.println("bindingResult.getAllErrors(): "+bindingResult.getAllErrors());
			System.out.println("addPersonDependant newDependant: "+newDependant);
			return bindingResult.getAllErrors().toString();
		}
		else
		{
			System.out.println("bindingResult.has-NO-Errors(): "+bindingResult.hasErrors());
		}
		
		System.out.println("newDependant: "+newDependant );
		
		Set<Integer> currDependantsSet = this.myDependantRepo.findByPersonId(personId)
				.stream().map( dep->dep.getDependantId() ).collect( Collectors.toSet() );
		
		System.out.println("currDependantsSet: "+currDependantsSet);
		
		if(newDependant.getDependantId()==0 || newDependant.getDependantId()==personId || currDependantsSet.contains( newDependant.getDependantId() ))
		{
//			model.addAttribute("content", "<h3>Error</h3><p>Invalid date of birth<br/><br/>Please try again.</p>");
			model.addAttribute("myTitle", "Error While Adding New Person Dependant");
			model.addAttribute("content", "Error\n\nInvalid Dependant.\n\n\n\nPlease try again.");
			return "personHome";
		}			

		
		this.myDependantRepo.save(newDependant);
		model.addAttribute("myTitle", "Successful Adding of Person Dependant");
		model.addAttribute("content", "Congrats\n\nYou have succesfully added a new Dependant:\n\n"+newDependant.toString()+"\n\nto person: " + myPerson.toString()+"\n\n...");
		return "personHome";
	}
	
	
	@GetMapping("/persons/{personId}/dependants/new/")
	public String personDependantNew(@PathVariable(name="personId") int personId,Model model)
	{
		model.addAttribute("body_class", "admin");
		model.addAttribute("myTitle", "Add New Dependant to Person");
		model.addAttribute("headNfoot", true);
		
		Person myPerson = this.myPersonRepo.findById(personId) ;
		model.addAttribute("myPerson", myPerson );
		
		Set<Integer> currDependantsSet = this.myDependantRepo.findByPersonId(personId)
				.stream().map( dep->dep.getDependantId() ).collect( Collectors.toSet() );
		
		List<Person> personOptions = this.myPersonRepo.findAll()
				.stream()
				.filter( dep-> dep.getId()!=myPerson.getId() )
				.filter( dep -> !currDependantsSet.contains( dep.getId() ) )
				.collect( Collectors.toList() );
		
		model.addAttribute("dependantOptions", personOptions );
		
//		System.out.println("personDependantNew: ");
//		System.out.println("dependantOptions: "+dependantOptions);
		
		return "register_person_dependant";
	}
	
}
