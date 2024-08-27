package com.example.demo.services.impl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.User;
import com.example.demo.helper.AppConstants;
import com.example.demo.helper.Helper;
import com.example.demo.helper.ResourceNotFoundException;
import com.example.demo.repositories.UserRepo;
import com.example.demo.services.EmailService;
import com.example.demo.services.UserService;


@Service
public class UserServiceImpl implements UserService {
	 @Autowired
	    private UserRepo userRepo;
	 
	 @Autowired
	 private PasswordEncoder passwordEncoder;
	 
	    private Logger logger = LoggerFactory.getLogger(this.getClass());

	 
	    @Autowired
	    private EmailService emailService;
	    
	 //finding the user
	 public Optional<User> getUserById(String id) {
	        return userRepo.findById(id);
	    }

	 //saving the user
	@Override
	public User saveUser(User user) {
		// TODO Auto-generated method stub
		  String userId = UUID.randomUUID().toString();
	        user.setUserId(userId);
	        
	        //encodes the password
	        user.setPassword(passwordEncoder.encode(user.getPassword()));

	        // set the user role
	        user.setRoleList(List.of(AppConstants.ROLE_USER));
	        
	        logger.info(user.getProvider().toString());
	        
	      //  return userRepo.save(user);
	       // User savedUser = userRepo.save(user);
	       // return savedUser;

	        logger.info(user.getProvider().toString());
	        String emailToken = UUID.randomUUID().toString();
	        user.setEmailToken(emailToken);
	        User savedUser = userRepo.save(user);
	        String emailLink = Helper.getLinkForEmailVerificatiton(emailToken);
	        emailService.sendEmail(savedUser.getEmail(), "Verify Account : Smart  Contact Manager", emailLink);
	        return savedUser;




	        
	}

	@Override
	public Optional<User> updateUser(User user) {
		// TODO Auto-generated method stub
		  User user2 = userRepo.findById(user.getUserId())
	                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	        // update karenge user2 from user
	        user2.setName(user.getName());
	        user2.setEmail(user.getEmail());
	        user2.setPassword(user.getPassword());
	        user2.setAbout(user.getAbout());
	        user2.setPhoneNumber(user.getPhoneNumber());
	        user2.setProfilePic(user.getProfilePic());
	        user2.setEnabled(user.isEnabled());
	        user2.setEmailVerified(user.isEmailVerified());
	        user2.setPhoneVerified(user.isPhoneVerified());
	        user2.setProvider(user.getProvider());
	        user2.setProviderUserId(user.getProviderUserId());
	        // save the user in database
	        User save = userRepo.save(user2);
	        return Optional.ofNullable(save);

	    }


	@Override
	public void deleteUser(String id) {
		 User user2 = userRepo.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	        userRepo.delete(user2);
		
	}

	@Override
	public boolean isUserExist(String userId) {
		 User user2 = userRepo.findById(userId).orElse(null);
	        return user2 != null ? true : false;
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isUserExistByEmail(String email) {
		 User user2 = userRepo.findByEmail(email).orElse(null);
	        return user2 != null ? true : false;
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		  return userRepo.findByEmail(email).orElse(null);

	}

	@Override
	public User getUserByName(String name) {
		// TODO Auto-generated method stub
		  return userRepo.findByName(name).orElse(null);
	}

	

}
