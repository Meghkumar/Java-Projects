package com.smart.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.Dao.ContactRepository;
import com.smart.Dao.UserRepository;
import com.smart.Helper.Message;
import com.smart.entities.Contact;
import com.smart.entities.User;

@Controller
@RequestMapping("/user")
public class UserController
	{

		@Autowired
		private UserRepository userRepository;

		@Autowired
		private ContactRepository contactRepository;

		@Autowired
		private BCryptPasswordEncoder passwordEncoder;

		@ModelAttribute
		public void addCommonData(Model model, Principal principal)
			{
				String username = principal.getName();
				System.out.println("Username:  " + username);

				User user = this.userRepository.getUserByName(username);

				model.addAttribute("user", user);

			}

		@RequestMapping("/index")
		public String dashboard(Model model, Principal principal)
			{
				model.addAttribute("title", "User DashBoard");

				return "normal/user_dashboard";

			}

		@GetMapping("/add-contact")
		public String openAddContactForm(Model model)
			{

				model.addAttribute("title", "Add contact");
				model.addAttribute("contact", new Contact());

				return "normal/add_contact_form";
			}

		@PostMapping("/process-contact")
		public String addcontact(@ModelAttribute Contact contact, @RequestParam("profile-image") MultipartFile file, Principal principal, Model model)
			{

				try
					{
						String username = principal.getName();

						User user = userRepository.getUserByName(username);

						// File storage

						if (file.isEmpty())
							{
								System.out.println("File is Empty");
								contact.setImage("contact.png");
							} else
							{

								contact.setImage(file.getOriginalFilename());

								File savefile = new ClassPathResource("static/image").getFile();

								Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());

								Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

								System.out.println("File is uploaded");

							}

						contact.setUser(user);
						user.getContact().add(contact);

						userRepository.save(user);
						model.addAttribute("message", new Message("Contact Added Successfully", "alert-success"));

						System.out.println(contact);
					} catch (Exception e)
					{
						model.addAttribute("message", new Message("Something went wrong Try Again", "alert-danger"));

						e.printStackTrace();
					}

				return "normal/add_contact_form";

			}

		@GetMapping("/showcontact/{page}")
		public String showcontact(@PathVariable("page") int page, Model model, Principal principal)
			{
				model.addAttribute("title", "Show user contacts");
				String username = principal.getName();
				User user = userRepository.getUserByName(username);

				Pageable pageable = PageRequest.of(page, 2);
				Page<Contact> contacts = contactRepository.getContactsByUserid(user.getId(), pageable);

				model.addAttribute("contacts", contacts);
				model.addAttribute("currentPage", page);
				model.addAttribute("totalPages", contacts.getTotalPages());

				return "normal/showcontact";

			}

		@GetMapping("/{cid}/contact")
		public String showContactDetail(@PathVariable("cid") int cid, Model model, Principal principal)
			{
				String username = principal.getName();

				User userByName = userRepository.getUserByName(username);

				Optional<Contact> contactID = contactRepository.findById(cid);
				Contact contact = contactID.get();

				if (userByName.getId() == contact.getUser().getId())
					{
						model.addAttribute("contact", contact);
					}
				return "/normal/contact_detail";
			}

		@GetMapping("/delete/{cid}")
		public String deleteContact(@PathVariable("cid") int cid, Model model, Principal principal)
			{
				String username = principal.getName();
				User user = userRepository.getUserByName(username);
				Optional<Contact> byId = contactRepository.findById(cid);
				Contact contact = byId.get();

				if (user.getId() == contact.getUser().getId())
					{
							{
								contactRepository.delete(contact);
							}

					}

				model.addAttribute("message", new Message("Contact Deleted Successfully...", "success"));

				return "redirect:/user/showcontact/0";
			}

		@PostMapping("update-contact/{cid}")
		public String updateDetail(@PathVariable("cid") int cid, Principal principal, Model model)
			{

				String username = principal.getName();

				Contact contact = contactRepository.findById(cid).get();

				model.addAttribute("contact", contact);

				model.addAttribute("title", "Update Contact");
				return "normal/udpate_form";
			}

		@PostMapping("/process-update")
		public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profile-image") MultipartFile file, Model model, Principal principal)
			{

				try
					{
						Contact oldcontact = contactRepository.findById(contact.getCid()).get();
						if (!file.isEmpty())
							{
								//delete old Image
								File deletefile = new ClassPathResource("static/image/").getFile();
								File file1 = new File(deletefile,oldcontact.getImage());
								file1.delete();

								
								//update new Image
								File savefile = new ClassPathResource("static/image/").getFile();
								Path path = Paths.get(savefile.getAbsoluteFile() + File.separator + file.getOriginalFilename());
								Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

								contact.setImage(file.getOriginalFilename());

							} else
							{
								contact.setImage(oldcontact.getImage());
							}

						User user = userRepository.getUserByName(principal.getName());
						contact.setUser(user);
						contactRepository.save(contact);

						model.addAttribute("message",new Message("Your Contact is updated","success"));
						
						System.out.println(contact.getName());
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				return "redirect:/user/"+contact.getCid()+"/contact";
			}
		
		@GetMapping("/profile")
		public String yourprofile(@ModelAttribute User user,Model model)
			{
				System.out.println(user);
				model.addAttribute("user", user);
				return "normal/profile";
			}
		
		@GetMapping("/setting")
		public String openSetting()
			{
				return "normal/setting";
				
			}
		
		@PostMapping("/change-password")
		public String changepassword(@RequestParam ("oldpassword") String oldpassword,@RequestParam("newpassword") String newpassword
				,Principal principal,Model model)
			{
				System.out.println(oldpassword);
				System.out.println(newpassword);
				
				String username= principal.getName();
				User user = userRepository.getUserByName(username);
				
				if(passwordEncoder.matches(oldpassword,user.getPassword())) {
					
					user.setPassword(passwordEncoder.encode(newpassword));
					userRepository.save(user);
					model.addAttribute("passmessage",new Message ("Your password has Successfuly changed" ,"alert-success"));
				}
				else {
					model.addAttribute("passmessage",new Message ("You have entered wrong old password" ,"alert-warning"));
					return "redirect:/user/setting";
				}
				
				
				return "redirect:/user/index";
			}
		
		
		
		

	}
