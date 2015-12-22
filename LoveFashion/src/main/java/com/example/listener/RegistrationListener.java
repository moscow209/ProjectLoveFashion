package com.example.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.entity.CustomerEntity;
import com.example.event.OnRegistrationCompleteEvent;
import com.example.service.ICustomerService;

@Component
public class RegistrationListener implements
		ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private ICustomerService service;

	@Autowired
	private JavaMailSender mailSender;

	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		CustomerEntity customer = event.getCustomer();
		String token = UUID.randomUUID().toString();
		service.createVerificationTokenForUser(customer, token);
		String recipientAddress = customer.getEmail();
		String subject = "Registration Confirmation";
		String confirmationUrl = event.getAppUrl()
				+ "/regitrationConfirm.html?token=" + token;
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText("Test" + ": " + "http://localhost:8080/customer/account"
				+ confirmationUrl);
		mailSender.send(email);
	}
}
