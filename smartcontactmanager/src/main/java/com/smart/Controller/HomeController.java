package com.smart.Controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.Dao.UserRepository;
import com.smart.Helper.Message;
import com.smart.entities.User;

@Controller
public class HomeController
	{

		@Autowired
		private BCryptPasswordEncoder passwordEncoder;

		@Autowired
		private UserRepository userRepository;

		@GetMapping("/home")
		public String home(Model model)
			{

				model.addAttribute("title", "Home - Smart Contact Manager");

				return "home";
			}

		@GetMapping("/about")
		public String about(Model model)
			{

				model.addAttribute("title", "Home - Smart Contact Manager");

				return "about";
			}

		@GetMapping("/signup")
		public String signup(Model model)
			{

				model.addAttribute("title", "Register - Smart Contact Manager");
				model.addAttribute("user", new User());

				return "signup";
			}

		@PostMapping("/do_register")
		public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
				@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, 
				Model model, HttpSession session)
			{

				if (bindingResult.hasErrors())
					{
						model.addAttribute("user", user);
						return "signup";
					}
				if (!agreement)
					{
						model.addAttribute("message", new Message("Kindly Check Terms & Conditions", "alert-danger"));
						return "signup";
					}
				try
					{
						user.setRole("ROLE_USER");
						user.setEnabled(true);
						user.setImageurl("aa");
						user.setPassword(passwordEncoder.encode(user.getPassword()));

						userRepository.save(user);
						model.addAttribute("user", new User());
						model.addAttribute("message", new Message("Successfully Registered", "alert-success"));

					} catch (Exception e)
					{
						e.printStackTrace();
						model.addAttribute("user", user);
						model.addAttribute("message", new Message("Something went wrong", "alert-danger"));
					}
				return "signup";
			}
		
		@GetMapping("/signin")
		public String cutomlogin(Model model)
			{
				
				model.addAttribute("title", "Login Page");
				return "login";
			}
		
		

	}
