package com.smart.Controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.EmailService;
import com.smart.Dao.UserRepository;
import com.smart.entities.User;

@Controller
public class ForgotController
	{
		@Autowired
		private EmailService emailservice;

		@Autowired
		private UserRepository userRepository;
		
		@Autowired
		private BCryptPasswordEncoder passwordEncoder;
		
		
		Random random = new Random(1000);

		@GetMapping("/forgot")
		public String openEmailForm()
			{
				
				return "forgot_email_form";
				
			}
		

		@PostMapping("/send-otp")
		public String semd_Otp(@RequestParam("email") String email,Model model,HttpSession session)
			{
				System.out.println(email);
				
				
				
				int otp = random.nextInt(99999);
				
				System.out.println(otp);
				
				String subject="OTP from SCM";
				String message="Your OTP is :-  '"+otp+"' ";
				String to=email;
				
				boolean flag = emailservice.Email(subject, message, to);
				
				
				if(flag) {
					session.setAttribute("myotp", otp);
					session.setAttribute("email", email);
					return "verify_otp";
	
				}
				
				
				else {
					model.addAttribute("message", "check your email Id");
					
					return "forgot_email_form";
				}
				

				
			}
		
		@PostMapping("/verify-otp")
		public String verifyOtp(@RequestParam("otp") int otp,HttpSession session,Model model)
			{
				
				
				Integer myotp =(Integer) session.getAttribute("myotp");
				String email =(String) session.getAttribute("email");
				
				
				if(otp==myotp) {
					
					User user = userRepository.getUserByName(email);
					
					if(user==null) {
						model.addAttribute("message", "User is not present");
						return "forgot_email_form";	
					}
					else {
						
						return "password_change_form";
					}
					
					
					
				}else {
					model.addAttribute("message", "OTP entered is not correct");
					return "verify_otp";
				}
				
				
			}
		
		@PostMapping("/change-password")
		public String changepassword(@RequestParam("newpassword") String newpassword,HttpSession session)
			{
				String email =(String) session.getAttribute("email");
				User user = userRepository.getUserByName(email);
				
				user.setPassword(passwordEncoder.encode(newpassword));
				userRepository.save(user);
				
				return "redirect:/signin?change=password changed successfully..";
			}
		
		
		
	}
