package com.example.service;

import java.security.NoSuchAlgorithmException;

import com.example.dto.RegisterModel;
import com.example.entity.CustomerEntity;
import com.example.entity.VerificationToken;

public interface ICustomerService {

	public CustomerEntity getCustomer(String email, String password)
			throws NoSuchAlgorithmException;

	public void update(CustomerEntity customer);

	public CustomerEntity register(RegisterModel register)
			throws NoSuchAlgorithmException;

	public void createVerificationTokenForUser(CustomerEntity customer,
			String token);

	public VerificationToken getVerificationToken(String verificationToken);

}
