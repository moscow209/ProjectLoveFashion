package com.example.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.RegisterModel;
import com.example.entity.CustomerEntity;
import com.example.entity.CustomerGroup;
import com.example.entity.VerificationToken;
import com.example.repository.ICustomerDAO;
import com.example.repository.IVerificationTokenDAO;

@Service
public class CustomerService implements ICustomerService {

	@Autowired
	private ICustomerDAO customerDao;
	@Autowired
	private IVerificationTokenDAO tokenRepository;

	@Transactional
	public CustomerEntity getCustomer(String email, String password)
			throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		password = hashPassword(password);
		return customerDao.getCustomer(email, password);
	}

	private String hashPassword(String password)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] hashedBytes = md.digest();
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < hashedBytes.length; i++) {
			stringBuffer.append(Integer.toString(
					(hashedBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return stringBuffer.toString();
	}

	@Transactional
	public void update(CustomerEntity customer) {
		// TODO Auto-generated method stub
		customerDao.update(customer);
	}

	@Transactional
	public CustomerEntity register(RegisterModel register)
			throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		if (emailExist(register.getEmail()))
			return null;
		CustomerEntity customer = new CustomerEntity();
		customer.setEmail(register.getEmail());
		customer.setFirstname(register.getFirstName());
		customer.setLastname(register.getLastName());
		customer.setPassword(hashPassword(register.getPassword()));
		customer.setIsActive((short) 0);
		customer.setCreatedAt(new Date());
		customer.setUpdatedAt(new Date());
		customer.setCustomerGroup(new CustomerGroup(Short.valueOf((short) 1)));
		customerDao.create(customer);
		return customer;
	}

	@SuppressWarnings("unused")
	@Transactional
	private boolean emailExist(String email) {
		CustomerEntity customer = customerDao.findByEmail(email);
		if (customer != null) {
			return true;
		}
		return false;
	}

	@Transactional
	public void createVerificationTokenForUser(CustomerEntity customer,
			String token) {
		// TODO Auto-generated method stub
		VerificationToken myToken = new VerificationToken(customer,
				"verify-email", token);
		tokenRepository.create(myToken);
	}

	@Transactional
	public VerificationToken getVerificationToken(String verificationToken) {
		// TODO Auto-generated method stub
		return tokenRepository.findByToken(verificationToken);
	}

}