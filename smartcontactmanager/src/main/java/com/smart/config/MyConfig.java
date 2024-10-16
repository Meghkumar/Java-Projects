package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class MyConfig extends WebSecurityConfigurerAdapter
	{

		@Override
		protected void configure(HttpSecurity http) throws Exception
			{
				http.
				authorizeRequests().
				antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/user/**").hasRole("USER")
				.antMatchers("/**").permitAll()
				.and()
				.formLogin()
						.loginPage("/signin")
						.loginProcessingUrl("/dologin")
						.defaultSuccessUrl("/user/index")
						//.failureUrl("/loginFail")
				.and().csrf().disable();
				

			}

	

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception
			{
				
				auth.authenticationProvider(authenticationProvider());
				
			}



		@Bean
		public BCryptPasswordEncoder passwordEncoder()
			{

				return new BCryptPasswordEncoder();

			}

		@Bean
		public UserDetailsService getuserDetailsService()
			{

				return new CustomUserDetailsService();

			}
		
		
		@Bean
		public DaoAuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
			daoAuthenticationProvider.setUserDetailsService(getuserDetailsService());
			daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		
			return daoAuthenticationProvider;
		}
		

	}
