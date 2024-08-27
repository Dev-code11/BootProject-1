package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.services.EmailService;

//@SpringBootTest
@SpringBootTest(properties = "spring.profiles.active=test")

class ApplicationTests {

	@Test
	void contextLoads() {
	}
	
	/*
	@Autowired
	private EmailService service;

	@Test
	void sendEmailTest() {
		service.sendEmail(
				"devenshpara18@gmail.com",
				"Just managing the emails and it's working",
				"this is scm project working on email service");
	}

*/
}
