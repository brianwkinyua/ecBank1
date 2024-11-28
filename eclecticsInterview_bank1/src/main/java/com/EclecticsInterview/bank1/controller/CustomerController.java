package com.EclecticsInterview.bank1.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.EclecticsInterview.bank1.model.CustomerTransaction;
import com.EclecticsInterview.bank1.model.MyUser;
import com.EclecticsInterview.bank1.model.Person;
import com.EclecticsInterview.bank1.model.UserAccountApplication;
import com.EclecticsInterview.bank1.repository.CustomerTransactionRepo;
import com.EclecticsInterview.bank1.repository.MyUserRepository;
import com.EclecticsInterview.bank1.repository.PersonRepo;
import com.EclecticsInterview.bank1.repository.UserAccountApplicationRepo;


//@RestController
@Controller
public class CustomerController {
	
//	@Autowired
	private MyUserRepository myUserRepository;
	private CustomerTransactionRepo myCustomerTransactionRepo;
	private UserAccountApplicationRepo myUserAccountApplicationRepo;
	private PersonRepo myPersonRepo;

	public CustomerController(MyUserRepository myUserRepository, CustomerTransactionRepo myCustomerTransactionRepo,
			UserAccountApplicationRepo myUserAccountApplicationRepo, PersonRepo myPersonRepo) {
		super();
		this.myUserRepository = myUserRepository;
		this.myCustomerTransactionRepo = myCustomerTransactionRepo;
		this.myUserAccountApplicationRepo = myUserAccountApplicationRepo;
		this.myPersonRepo = myPersonRepo;
	}
	
	public static boolean isNumeric(String str) {
	    if (str == null || str.isEmpty()) {
	        return false;
	    }
	    try {
	        Double.parseDouble(str); // Parses the string to a double
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
	
	public static boolean isNumber(Object obj) {
	    return obj instanceof Number; // Works for Integer, Double, Float, etc.
	}


	@RequestMapping(
			  path = "/customers/{customerId}/transactions/new/", 
			  method = RequestMethod.POST,
			  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
			  produces = {
			    MediaType.APPLICATION_ATOM_XML_VALUE, 
			    MediaType.APPLICATION_JSON_VALUE,
			    MediaType.TEXT_HTML_VALUE
			  })
//	@PostMapping("/persons/new/")
//	@ResponseBody
	public String addCustomerTransaction(@PathVariable(name="customerId") int customerId, Model model, CustomerTransaction newCustomerTransaction, BindingResult bindingResult)
	{
		model.addAttribute("body_class", "user");
		model.addAttribute("myTitle", "Add New Customer Transaction");
		model.addAttribute("headNfoot", true);
		
		MyUser myUser = this.myUserRepository.findById(customerId) ;
		model.addAttribute("myUser", myUser );
		System.out.println("bindingResult: "+bindingResult);
		
		if (bindingResult.hasErrors())
		{
			System.out.println("bindingResult.hasErrors(): "+bindingResult.hasErrors());
			System.out.println("bindingResult.getAllErrors(): "+bindingResult.getAllErrors());
			System.out.println("addCustomerTransaction newCustomerTransaction: "+newCustomerTransaction);
			return bindingResult.getAllErrors().toString();
		}
		else
		{
			System.out.println("bindingResult.has-NO-Errors(): "+bindingResult.hasErrors());
		}
		
		System.out.println("newCustomerTransaction: "+newCustomerTransaction );
		
		if (newCustomerTransaction.getTransactionType().equals("Check Balance")
				&& newCustomerTransaction.getAmount() != null 
			)
		{
//			model.addAttribute("content", "<h3>Error</h3><p>Invalid date of birth<br/><br/>Please try again.</p>");
			model.addAttribute("myTitle", "Error While Adding New Customer Transaction");
			model.addAttribute("content", "Error\n\nChecking balance needs no amount.\n\n\n\nPlease try again.");
			return "customerHome";
		}
		else
		if ( ! newCustomerTransaction.getTransactionType().equals("Check Balance")
				&& ( newCustomerTransaction.getAmount() == null || 
				newCustomerTransaction.getAmount().toString().isEmpty() ||
				! isNumber(newCustomerTransaction.getAmount() ) 
				)
			)
		{
//			model.addAttribute("content", "<h3>Error</h3><p>Invalid date of birth<br/><br/>Please try again.</p>");
			model.addAttribute("myTitle", "Error While Adding New Customer Transaction");
			model.addAttribute("content", "Error\n\n" +newCustomerTransaction.getTransactionType() + " needs an amount.\n\n\n\nPlease try again.");
			return "customerHome";
		}
		
		this.myCustomerTransactionRepo.save(newCustomerTransaction);
		model.addAttribute("myTitle", "Successful Adding of Customer Transaction");
		model.addAttribute("content", "Congrats\n\nYou have succesfully added a new Customer Transaction:\n\n"+newCustomerTransaction.toString()+"\n\nto user: " + myUser.toString()+"\n\n...");
		return "customerHome";
	}
	
	
	@GetMapping("/customers/{customerId}/transactions/new/")
	public String customerTransactionNew(@PathVariable(name="customerId") int customerId,Model model)
	{
		model.addAttribute("body_class", "user");
		model.addAttribute("myTitle", "Add New Customer Transaction");
		model.addAttribute("headNfoot", true);
		
		MyUser myUser = this.myUserRepository.findById(customerId) ;
		model.addAttribute("myUser", myUser );
		
		List<String> transactionTypeOptions = new ArrayList<>();
		transactionTypeOptions.add("Check Balance");
		transactionTypeOptions.add("Funds Transfer");
		transactionTypeOptions.add("Pay Bill");
		transactionTypeOptions.add("Buy Investment");
		transactionTypeOptions.add("Sell Investment");
		transactionTypeOptions.add("Take Loan");
		transactionTypeOptions.add("Pay Loan");
		
		model.addAttribute("transactionTypeOptions", transactionTypeOptions );
		
		return "register_CustomerTransaction";
	}
	
	
	@GetMapping("/customers/{customerId}/")
	public String showOneCustomer(@PathVariable(name="customerId") int customerId, Model model)
	{
		model.addAttribute("body_class", "user");
		System.out.println("showOneCustomer | pid: "+customerId);
		model.addAttribute("myTitle", "Customer Details");
		model.addAttribute("headNfoot", true);
		
		model.addAttribute("myUserName", SecurityContextHolder.getContext().getAuthentication().getName()  );
		model.addAttribute("myUserRole", SecurityContextHolder.getContext().getAuthentication().getAuthorities()  );
		
		MyUser myUser = this.myUserRepository.findById(customerId) ;
		model.addAttribute("myUser", myUser );
		model.addAttribute("customerTransactions", this.myCustomerTransactionRepo.findByCustomerId(customerId) );
		
		UserAccountApplication myUserAccountApplication = myUserAccountApplicationRepo.findByUserAccountId(customerId);
		int ownerPersonId = myUserAccountApplication.getPersonId();
		
		Person ownerPersonObj = myPersonRepo.findById(ownerPersonId);
		model.addAttribute("ownerPersonObj", ownerPersonObj );
		
		return "showOneCustomer";
	}
	
	
}
