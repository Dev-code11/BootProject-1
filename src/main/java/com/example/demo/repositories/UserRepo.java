package com.example.demo.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.User;

@Repository
public interface UserRepo  extends JpaRepository<User, String> {

	Optional<User> findByEmail(String email);
	Optional<User> findByName(String name);
    // extra methods db relatedoperations
    // custom query methods
    // custom finder methods

  //  Optional<User> findByEmail(String email);

   Optional<User> findByEmailAndPassword(String email, String password);

   Optional<User> findByEmailToken(String id);

}