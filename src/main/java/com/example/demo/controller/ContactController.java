package com.example.demo.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Entities.Contacts;
import com.example.demo.Entities.User;
import com.example.demo.Forms.ContactForm;
import com.example.demo.Forms.ContactSearchForm;
import com.example.demo.helper.AppConstants;
import com.example.demo.helper.Helper;
import com.example.demo.helper.Message;
import com.example.demo.helper.MessageType;
import com.example.demo.services.ContactService;
import com.example.demo.services.ImageService;
import com.example.demo.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
    private UserService userService;
	
	  @Autowired
	    private ImageService imageService;

	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(ContactController.class);

	

	
	
	
	 @RequestMapping("/add")
	    // add contact page: handler
	    public String addContactView(Model model) {
	        ContactForm contactForm = new ContactForm();

	        contactForm.setFavorite(true);
	        model.addAttribute("contactForm", contactForm);
	        return "user/add_contact";
	    }

	 @RequestMapping(value = "/add", method = RequestMethod.POST)
	    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
	            Authentication authentication, HttpSession session) {

	       //validation
	        if (result.hasErrors()) {

	            result.getAllErrors().forEach(error -> logger.info(error.toString())); //prints the error on console with the help of logger

	            session.setAttribute("message", Message.builder()
	                    .content("Please correct the following errors")
	                    .type(MessageType.red)
	                    .build());
	            return "user/add_contact";
	        }
	     // form ---> contact
	        String username = Helper.getEmailOfLoggedInUser(authentication);
	       User user = userService.getUserByEmail(username);
	    
	        Contacts contact = new Contacts();
	        contact.setName(contactForm.getName());
	        contact.setFavorite(contactForm.isFavorite());
	        contact.setEmail(contactForm.getEmail());
	        contact.setPhoneNumber(contactForm.getPhoneNumber());
	        contact.setAddress(contactForm.getAddress());
	        contact.setDescription(contactForm.getDescription());
	        contact.setUser(user);
	        contact.setLinkedInLink(contactForm.getLinkedInLink());
	        contact.setWebsiteLink(contactForm.getWebsiteLink());
	        
	        //if block for image

	        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
	            String filename = UUID.randomUUID().toString();
	            String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);
	            contact.setPicture(fileURL);
	            contact.setCloudinaryImagePublicId(filename);
	            }
	        contactService.save(contact);
	        System.out.println(contactForm);

	        // 3 set the contact picture url

	        // 4 `set message to be displayed on the view

	        session.setAttribute("message",
	                Message.builder()
	                        .content("You have successfully added a new contact")
	                        .type(MessageType.green)
	                        .build());

	        return "redirect:/user/contacts/add";

	 } 
	 
	 
	 @RequestMapping
	    public String viewContacts(
	            @RequestParam(value = "page", defaultValue = "0") int page,
	            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
	            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
	            @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,
	            Authentication authentication) {

	        // load all the user contacts
	        String username = Helper.getEmailOfLoggedInUser(authentication);
	        User user = userService.getUserByEmail(username);

	        Page<Contacts> pageContact = contactService.getByUser(user, page, size, sortBy, direction);

	        model.addAttribute("pageContact", pageContact);
	        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

	        model.addAttribute("contactSearchForm", new ContactSearchForm());

	        return "user/contacts";
}
	 
	 @RequestMapping("/search")
	    public String searchHandler(

	           @Valid @ModelAttribute ContactSearchForm contactSearchForm, BindingResult result, HttpSession session,
	            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
	            @RequestParam(value = "page", defaultValue = "0") int page,
	            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
	            @RequestParam(value = "direction", defaultValue = "asc") String direction,
	            Model model, 
	            Authentication authentication) {
		 
		 //validation
		 /*
	        if (result.hasErrors()) {

	            result.getAllErrors().forEach(error -> logger.info(error.toString())); //prints the error on console with the help of logger

	            session.setAttribute("message", Message.builder()
	                    .content("Please correct the following errors")
	                    .type(MessageType.red)
	                    .build());
	            return "user/search";
	        }
	        */

	        logger.info("field {} keyword {}", contactSearchForm.getField(), contactSearchForm.getValue());

	        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
	        
	       

	        Page<Contacts> pageContact = Page.empty(); 
	        
	       
       
	      //  Page<Contacts> pageContact;
	        if (contactSearchForm.getField().equalsIgnoreCase("name")) {
	            pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,
	                    user);
	        } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
	            pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,
	                    user);
	        } else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
	            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy,
	                    direction, user);
	        }
	     //   if (pageContact == null || pageContact.getTotalElements() == 0) {
	            // Handle the case when no results are found
	           // model.addAttribute("message", "Invalid Search");
	      //      model.addAttribute("contactSearchForm", contactSearchForm);
	          //  return "user/contacts"; // Return to a specific view for no results
	       // }
	 

	        logger.info("pageContact {}", pageContact);

	        model.addAttribute("contactSearchForm", contactSearchForm);

	        model.addAttribute("pageContact", pageContact);

	        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

	        return "user/search";
	 
	 
	 } 
	 
	 // detete contact
	 
	    @RequestMapping("/delete/{contactId}")
	    public String deleteContact(
	            @PathVariable("contactId") String contactId,
	            HttpSession session) {
	        contactService.delete(contactId);
	        logger.info("contactId {} deleted", contactId);

	        session.setAttribute("message",
	                Message.builder()
	                        .content("Contact is Deleted successfully !! ")
	                        .type(MessageType.green)
	                        .build()

	        );

	        return "redirect:/user/contacts";
	    }
	    
	 // update contact form view
	    @GetMapping("/view/{contactId}")
	    public String updateContactFormView(
	            @PathVariable("contactId") String contactId,
	            Model model) {

	        var contact = contactService.getById(contactId);
	        ContactForm contactForm = new ContactForm();
	        
	        //contact form mai isliye set kiya taki jab user form khole toh usse
	        //values already dali huii mile
	        
	        contactForm.setName(contact.getName());
	        contactForm.setEmail(contact.getEmail());
	        contactForm.setPhoneNumber(contact.getPhoneNumber());
	        contactForm.setAddress(contact.getAddress());
	        contactForm.setDescription(contact.getDescription());
	        contactForm.setFavorite(contact.isFavorite());
	        contactForm.setWebsiteLink(contact.getWebsiteLink());
	        contactForm.setLinkedInLink(contact.getLinkedInLink());
	        contactForm.setPicture(contact.getPicture());
	        ;
	        model.addAttribute("contactForm", contactForm);
	        model.addAttribute("contactId", contactId);

	        return "user/update_contact_view";
	    }
	    
	    //UPDATE CONTACT!!
	    
	    @RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
	    public String updateContact(@PathVariable("contactId") String contactId,
	            @Valid @ModelAttribute ContactForm contactForm,
	            BindingResult bindingResult,
	            Model model, HttpSession session) {

	        // update the contact
	        if (bindingResult.hasErrors()) {
	            return "user/update_contact_view";
	        }
//contactId se vo contact mil gya jiske andar data set karna hain 
	        var con = contactService.getById(contactId);
	        con.setId(contactId);
	        con.setName(contactForm.getName());
	        con.setEmail(contactForm.getEmail());
	        con.setPhoneNumber(contactForm.getPhoneNumber());
	        con.setAddress(contactForm.getAddress());
	        con.setDescription(contactForm.getDescription());
	        con.setFavorite(contactForm.isFavorite());
	        con.setWebsiteLink(contactForm.getWebsiteLink());
	        con.setLinkedInLink(contactForm.getLinkedInLink());

	        // process image:

	        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
	            logger.info("file is not empty");
	            String fileName = UUID.randomUUID().toString();
	            String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
	            con.setCloudinaryImagePublicId(fileName);
	            con.setPicture(imageUrl);
	            contactForm.setPicture(imageUrl);

	        } else {
	            logger.info("file is empty");
	        }
//con jiske andar pehle ka data tha usko pass kardiya update method k pass
	        var updateCon = contactService.update(con);
	        logger.info("updated contact {}", updateCon);

	        session.setAttribute("message", Message.builder().content("Contact Updated !!").type(MessageType.green).build());

	        return "redirect:/user/contacts/view/" + contactId;
	    }
	 
	 
	 
	 
	 

}
