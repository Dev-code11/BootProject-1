package com.example.demo.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.Entities.Contacts;
import com.example.demo.Entities.User;


public interface ContactService {
	
	 // save contacts
    Contacts save(Contacts contact);

    // update contact
    Contacts update(Contacts contact);

    // get contacts
    List<Contacts> getAll();

    // get contact by id

    Contacts getById(String id);

    // delete contact

    void delete(String id);
    
    // get contacts by userId
    List<Contacts> getByUserId(String userId);
    
    List<Contacts> getByUser(User user);
    
    Page<Contacts> getByUser(User user, int page, int size, String sortBy, String direction);
    
    // search contact
    Page<Contacts> searchByName(String nameKeyword, int size, int page, String sortBy, String order, User user);

    Page<Contacts> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order, User user);

    Page<Contacts> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String order,
            User user);



}
