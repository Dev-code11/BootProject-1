package com.example.demo.controller;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Entities.User;
import com.example.demo.helper.Message;
import com.example.demo.helper.MessageType;
import com.example.demo.repositories.UserRepo;
import com.example.demo.services.UserService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/user")
public class UserController {
	
	
	
	
	

	
	    private Logger logger = LoggerFactory.getLogger(UserController.class);

	    @Autowired
	    private UserService userService;
	    
	    @Autowired
		private BCryptPasswordEncoder bcpe;
	    
	    @Autowired
		private UserRepo userepo;

	    // user dashbaord page

	    @RequestMapping(value = "/dashboard")
	    public String userDashboard() {
	        System.out.println("User dashboard");
	        return "user/dashboard";
	    }


	    @RequestMapping(value = "/profile")
	    public String userProfile(Model model, Authentication authentication) {
	    	
	        return "user/profile";
	    }
	    
	    @RequestMapping("/settings")
	    public String ShowSetting(Model model, Authentication authentication) {
	    	
	        return "user/setting";
	    }
	    
	    @PostMapping("/update-password")
	    public String updatePass(@RequestParam("current-password") String cp, @RequestParam("new-password") String np,
	    		Authentication authentication,Principal princi, HttpSession session) {
	        System.out.println("current-password "+cp);
	        System.out.println("new-password "+np);
String name= princi.getName(); //this returns the email
System.out.println("Username "+name);
User user= userService.getUserByEmail(name);
System.out.println(user.getPassword());


if(cp.equals(np)) {
	
	System.out.println("New password can't be same as old password");


	 Message message = Message.builder().content("New password can't be same as old password").type(MessageType.red).build();

   session.setAttribute("message", message); 
   return "user/setting";
	}

if(this.bcpe.matches(cp, user.getPassword())){
	user.setPassword(this.bcpe.encode(np));
	this.userepo.save(user);
	System.out.println("Password Reset Successful");
	 Message message = Message.builder().content("Password Reset Successful").type(MessageType.green).build();

     session.setAttribute("message", message); 
     return "user/setting";
}

	        return "user/setting";
	    }
	    
	    @PostMapping("/update-username")
	    public String updateUsername(@RequestParam("username") String username,
	    		Authentication authentication,Principal princi, HttpSession session, User user) {
	    	
	    	 String oldname;
	    	 String name; //name here means  email
	    	    if (authentication.getPrincipal() instanceof OAuth2User) {
	    	        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
	    	        name = oauthUser.getAttribute("email"); // or use other attributes if needed
	    	         user= userService.getUserByEmail(name);
	    	        oldname=user.getName();
	    		   // String oldname=	user.getName();
	    	       // oldname=oauthUser.getName();
	    	        
	    	    }
	    	    if (authentication.getPrincipal() instanceof OAuth2User) {
	    	        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
	    	        name = oauthUser.getAttribute("name"); // or use other attributes if needed
	    	         user= userService.getUserByName(name);
	    	        oldname=user.getName();
	    		   // String oldname=	user.getName();
	    	       // oldname=oauthUser.getName();
	    	        
	    	    }
	    	    else {
	    	        name = authentication.getName(); // for standard users
	    	         user= userService.getUserByEmail(name);
	    	        oldname=user.getName();
	    	        //oldname=	user.getName();
	    	    }

	    	
	    //	String name= princi.getName();
	    	//User user= userService.getUserByEmail(name);
	   // String oldname=	user.getName();
	    System.out.println("Newname "+username);
	    System.out.println("Oldname "+oldname);
	    
	    if(oldname.equals(username)) {
	    	
	    	System.out.println("New name can't be same as old name");


	    	 Message message = Message.builder().content("New name can't be same as old name").type(MessageType.red).build();

	       session.setAttribute("message", message); 
	       return "user/setting";
	    	}
	    else {
	    	user.setName(username);
	    	this.userepo.save(user);
	    	
	    	System.out.println("Newname "+username);
	    	System.out.println("Password Reset Successful");
	   	 Message message = Message.builder().content("Username Reset Successful").type(MessageType.green).build();

	        session.setAttribute("message", message); 
	        
	    }
	    
	    	return "user/setting";
	    }
	    
	    @RequestMapping("/feedback")
	    public String SendFeedback(Model model, Authentication authentication) {
	    	
	        return "user/feedback";	   
	        }
	    @RequestMapping("/chatbot")
	    public String ShowBot() {
	    	
	        return "user/bot";	   
	        }
	    
}
