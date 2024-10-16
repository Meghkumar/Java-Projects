package com.smart.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.Dao.ContactRepository;
import com.smart.Dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

@RestController
public class SearchController
	{
		@Autowired
		private ContactRepository contactRepository;
		
		@Autowired
		private UserRepository userRepository ;
		
		@GetMapping("/search/{query}")
		public ResponseEntity<?> searchContact(@PathVariable("query")String query ,Principal principal)
			{
				User user  = userRepository.getUserByName(principal.getName());
				List<Contact> contacts = contactRepository.findByNameContainingAndUser(query, user);
				
				return ResponseEntity.ok(contacts);
			}
		
		
	}
