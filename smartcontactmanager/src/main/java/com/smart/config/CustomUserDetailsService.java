package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.Dao.UserRepository;
import com.smart.entities.User;

public class CustomUserDetailsService implements UserDetailsService
	{
		@Autowired
		private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
		{
			
			User user = userRepository.getUserByName(username);
			
			if(user == null) {
				
				throw new UsernameNotFoundException("User not Found");
			}
			
			CustomUserDetails customDetails = new CustomUserDetails(user);
			
			
			return customDetails;
		}

	}
