package com.smart.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "User_Details")
public class User
	{

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private int id;

		@NotBlank(message = "Name Should not be blank")
		@Size(min = 3, max = 20, message = "Min Char Should be 3 and Max should be 20")
		private String name;

		@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "kindly check the email you have entered")
		private String email;

		@Size(min = 3, max = 20, message = "Min Char Should be 3 and Max should be 20")
		private String password;
		private String role;
		private boolean enabled;
		private String imageurl;

		@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
		private List<Contact> contact = new ArrayList<>();

		@Column(length = 500)
		private String about;

		public int getId()
			{
				return id;
			}

		public void setId(int id)
			{
				this.id = id;
			}

		public String getName()
			{
				return name;
			}

		public void setName(String name)
			{
				this.name = name;
			}

		public String getEmail()
			{
				return email;
			}

		public void setEmail(String email)
			{
				this.email = email;
			}

		public String getPassword()
			{
				return password;
			}

		public void setPassword(String password)
			{
				this.password = password;
			}

		public String getRole()
			{
				return role;
			}

		public void setRole(String role)
			{
				this.role = role;
			}

		public boolean isEnabled()
			{
				return enabled;
			}

		public void setEnabled(boolean enabled)
			{
				this.enabled = enabled;
			}

		public String getImageurl()
			{
				return imageurl;
			}

		public void setImageurl(String imageurl)
			{
				this.imageurl = imageurl;
			}

		public List<Contact> getContact()
			{
				return contact;
			}

		public void setContact(List<Contact> contact)
			{
				this.contact = contact;
			}

		public String getAbout()
			{
				return about;
			}

		public void setAbout(String about)
			{
				this.about = about;
			}

		public User()
			{
				super();
				// TODO Auto-generated constructor stub
			}

		public User(int id, @NotBlank(message = "Name Should not be blank") @Size(min = 3, max = 20, message = "Min Char Should be 3 and Max should be 20") String name,
				@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "kindly check the email you have entered") String email,
				@Size(min = 3, max = 20, message = "Min Char Should be 3 and Max should be 20") String password, String role, boolean enabled, String imageurl, List<Contact> contact, String about)
			{
				super();
				this.id = id;
				this.name = name;
				this.email = email;
				this.password = password;
				this.role = role;
				this.enabled = enabled;
				this.imageurl = imageurl;
				this.contact = contact;
				this.about = about;
			}

	}
