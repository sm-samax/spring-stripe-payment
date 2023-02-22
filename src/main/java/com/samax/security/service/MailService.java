package com.samax.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.samax.security.constants.MailConstants;
import com.samax.security.model.User;

@Service
public class MailService {
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private VerificationURLService verificationURLService;
	
	public void sendVerificationMessage(User user) {
		MimeMessagePreparator preparator = mimeMessage -> {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
			message.setFrom(MailConstants.FROM);
			message.setTo(user.getEmail());
			message.setSubject(MailConstants.VERIFICATION_SUBJECT);
			
			ClassPathResource template = new ClassPathResource("mail_template/verification.html");
			String body = new String(template.getInputStream().readAllBytes());
			
			String verificationUrl = verificationURLService.generateVerificationUrl(user);
			
			body = body.replaceAll(MailConstants.URL_PARAM, verificationUrl);
			
			message.setText(body, true);
		};
		mailSender.send(preparator);
	}
	
	public void sendVerificationSuccessMessage(User user) {
		MimeMessagePreparator preparator = mimeMessage -> {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
			message.setFrom(MailConstants.FROM);
			message.setTo(user.getEmail());
			message.setSubject(MailConstants.VERIFICATION_SUCCESS_SUBJECT);
			
			ClassPathResource template = new ClassPathResource("mail_template/verification_success.html");
			String body = new String(template.getInputStream().readAllBytes());
			
			message.setText(body, true);
		};
		mailSender.send(preparator);
	}
}
