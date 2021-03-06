package com.example.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.AddressAccount;
import com.example.dto.RegisterModel;
import com.example.entity.CustomerAddressEntity;
import com.example.entity.CustomerEntity;
import com.example.entity.CustomerGroup;
import com.example.entity.VerificationToken;
import com.example.repository.ICustomerAddressDAO;
import com.example.repository.ICustomerDAO;
import com.example.repository.IVerificationTokenDAO;

@Service
public class CustomerService implements ICustomerService {

	@Autowired
	private ICustomerDAO customerDao;
	@Autowired
	private IVerificationTokenDAO tokenRepository;
	@Autowired
	private ICustomerAddressDAO addressRepository;

	@Transactional
	public CustomerEntity getCustomer(String email, String password)
			throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		password = hashPassword(password);
		return customerDao.getCustomer(email, password);
	}

	public String hashPassword(String password) throws NoSuchAlgorithmException {
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
			String token, String type) {
		// TODO Auto-generated method stub
		VerificationToken myToken = new VerificationToken(customer,
				type, token);
		tokenRepository.create(myToken);
	}
	
	public VerificationToken generateNewVerificationToken(String existingToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingToken);
        vToken.updateToken(UUID.randomUUID().toString());
        tokenRepository.update(vToken);
        return vToken;
    }

	@Transactional
	public VerificationToken getVerificationToken(String verificationToken) {
		// TODO Auto-generated method stub
		return tokenRepository.findByToken(verificationToken);
	}

	@Transactional
	public void saveAdress(AddressAccount address, CustomerEntity customer) {
		// TODO Auto-generated method stub
		CustomerAddressEntity cusAddress = new CustomerAddressEntity();
		cusAddress.setCustomerEntity(customer);
		cusAddress.setCountry(address.getCountry());
		cusAddress.setCountryId(address.getCountryId());
		cusAddress.setRegion(address.getRegion());
		cusAddress.setRegionId(address.getRegionId());
		cusAddress.setCreatedAt(new Date());
		cusAddress.setUpdatedAt(new Date());
		cusAddress.setFirstname(address.getFirstName());
		cusAddress.setLastname(address.getLastName());
		cusAddress.setPhone(address.getTelephone());
		cusAddress.setStreet(address.getStreet());
		int id = addressRepository.create(cusAddress);
		if (address.isDefaultBillingAddress())
			customer.setDefaultBilling(id);
		if (address.isDefaultShippingAddress())
			customer.setDefaultShipping(id);
		this.update(customer);
	}

	@Transactional
	public CustomerAddressEntity getCustomerAddress(Integer id) {
		// TODO Auto-generated method stub
		return addressRepository.get(id);
	}

	@Transactional
	public List<CustomerAddressEntity> findAdditionalAddress(int customerId,
			Integer defaultBilling, Integer defaultShipping) {
		// TODO Auto-generated method stub
		return addressRepository.findAdditionalAddress(customerId,
				defaultBilling, defaultShipping);
	}

	@Transactional
	public void deleteCustomerAddress(Integer id) {
		// TODO Auto-generated method stub
		addressRepository.deleteById(id);
	}

	@Transactional
	public void updateAdress(AddressAccount address, CustomerEntity customer) {
		// TODO Auto-generated method stub
		CustomerAddressEntity cusAddress = new CustomerAddressEntity();
		cusAddress.setCustomerEntity(customer);
		cusAddress.setCountry(address.getCountry());
		cusAddress.setCountryId(address.getCountryId());
		cusAddress.setRegion(address.getRegion());
		cusAddress.setRegionId(address.getRegionId());
		cusAddress.setUpdatedAt(new Date());
		cusAddress.setFirstname(address.getFirstName());
		cusAddress.setLastname(address.getLastName());
		cusAddress.setPhone(address.getTelephone());
		cusAddress.setStreet(address.getStreet());
		addressRepository.update(cusAddress);
		if (address.isDefaultBillingAddress())
			customer.setDefaultBilling(address.getEntityId());
		if (address.isDefaultShippingAddress())
			customer.setDefaultShipping(address.getEntityId());
		this.update(customer);
	}

	@Transactional
	public CustomerEntity findByEmail(String email) {
		// TODO Auto-generated method stub
		return customerDao.findByEmail(email);
	}

	@Transactional
	public CustomerEntity getCustomerId(Integer id) {
		// TODO Auto-generated method stub
		return customerDao.get(id);
	}

}
