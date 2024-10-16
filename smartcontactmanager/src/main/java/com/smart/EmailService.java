package com.smart;

import java.io.File;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Service;

@Service
public class EmailService
	{

		public boolean Email(String subject, String message, String to)
			{
				boolean f = false;

				String host = "smtp.gmail.com";
				//to = "meghkapale1994@gmail.com";

				Properties properties = System.getProperties();

				properties.put("mail.smtp.host", host);
				properties.put("mail.smtp.port", "465");
				properties.put("mail.smtp.auth", "true");
				properties.put("mail.smtp.ssl.enable", "true");
				properties.put("mail.smtp.starttls.enable", "true");

				Session session = Session.getInstance(properties, new javax.mail.Authenticator()
					{

						@Override
						protected PasswordAuthentication getPasswordAuthentication()
							{

								return new javax.mail.PasswordAuthentication("meghkapale1994@gmail.com", "dgmu zkqw gbtl yiql");
							}

					});

				session.setDebug(true);
				MimeMessage m = new MimeMessage(session);

				try
					{

						m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
						m.setSubject(subject);
						String path = "C:\\Users\\Megh\\Desktop\\Sample Images\\image2.jpg";
						MimeMultipart mime = new MimeMultipart();
						MimeBodyPart mimetext = new MimeBodyPart();
						MimeBodyPart mimeFile = new MimeBodyPart();

						try
							{

								mimetext.setText(message);
								File file = new File(path);

								mimeFile.attachFile(file);

								mime.addBodyPart(mimeFile);
								mime.addBodyPart(mimetext);

							} catch (Exception e)
							{
								e.printStackTrace();
							}

						m.setContent(mime);
						Transport.send(m);

						f = true;

					} catch (MessagingException e)
					{
						e.printStackTrace();
					}
				return f;
			}
	}
