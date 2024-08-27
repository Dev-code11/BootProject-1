package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Entities.Contacts;
import com.example.demo.Entities.User;

public interface ContactRepo extends JpaRepository<Contacts, String>{
	
	 // find the contact by user
    // custom finder method
   List<Contacts> findByUser(User user);
	 Page<Contacts> findByUser(User user, Pageable pageable);

    // custom query method
    @Query("SELECT c FROM Contacts c WHERE c.user.id = :userId")
    List<Contacts> findByUserId(@Param("userId") String userId);

    Page<Contacts> findByUserAndNameContaining(User user, String namekeyword, Pageable pageable);

    Page<Contacts> findByUserAndEmailContaining(User user, String emailkeyword, Pageable pageable);

    Page<Contacts> findByUserAndPhoneNumberContaining(User user, String phonekeyword, Pageable pageable);
}
