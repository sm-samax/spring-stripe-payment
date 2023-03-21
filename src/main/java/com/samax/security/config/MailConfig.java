package com.samax.security.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.samax.security.constants.MailConstants;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class MailConfig {

	@Autowired
	private Dotenv dotenv;
	
	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(MailConstants.GMAIL_SMTP);
		mailSender.setPort(MailConstants.GMAIL_PORT);

		mailSender.setUsername(dotenv.get("MAIL_USERNAME"));
		mailSender.setPassword(dotenv.get("MAIL_PASSWORD"));

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}
}
