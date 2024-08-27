package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.Entities.User;
import com.example.demo.Forms.UserForm;
import com.example.demo.helper.Message;
import com.example.demo.helper.MessageType;
import com.example.demo.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageControl {
	
	 @Autowired
	    private UserService userService;
	
	@RequestMapping("/hom")
	public String HomeTest(Model model) {
		model.addAttribute("model", "Devansh is....THAT GUY!");
		return "hom.html";
		
	}
	
	@RequestMapping("/base")
	public String BaseTest(Model model) {
		model.addAttribute("model", "Devansh is....THAT GUY!");
		return "base.html";
		
	}
	
	@RequestMapping("/about")
	public String AboutTest(Model model) {
		model.addAttribute("model", "Devansh is....THAT GUY!");
		return "about.html";
		
	}
	

	@RequestMapping("/serv")
	public String ServiceTest(Model model) {
		model.addAttribute("model", "Devansh is....THAT GUY!");
		return "services.html";
		
	}
	
	@RequestMapping("/con")
	public String ConTest(Model model) {
		model.addAttribute("model", "Devansh is....THAT GUY!");
		return "contact.html";
		
	}
	
	@RequestMapping("/login")
	public String LogTest(Model model) {
		model.addAttribute("model", "Devansh is....THAT GUY!");
		return "login.html";
		
	}
	
	@RequestMapping("/sign")
	public String SinTest(Model model) {
		  UserForm userForm = new UserForm();
	        // default data bhi daal sakte hai
	        // userForm.setName("Durgesh");
	        // userForm.setAbout("This is about : Write something about yourself");
	        model.addAttribute("userForm", userForm);
	       

	        
		return "signup.html";
		
	}
	
	@RequestMapping(value = "/sign", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult,
            HttpSession session) {
        System.out.println("Processing registration");
        // fetch form data
        // UserForm
        System.out.println(userForm);

        // validate form data
        if (rBindingResult.hasErrors()) {
            return "signup";
        }
        User user = new User();
       
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false);
        user.setProfilePic(
                "https://www.kindpng.com/picc/m/459-4595992_business-user-account-png-image-blue-link-icon.png");
 //jab user dashboard bnauga toh iska dhyan rakhna hoga
        
        User savedUser = userService.saveUser(user);

        System.out.println("user saved :"+savedUser);

        // message = "Registration Successful"

        // add the message:

        Message message = Message.builder().content("Verification link is send to your email").type(MessageType.green).build();

        session.setAttribute("message", message); 

        // redirectto login page
        return "redirect:/sign";
    }

	

}
