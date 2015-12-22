package com.example.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.example.dto.AddressAccount;
import com.example.dto.RegisterModel;
import com.example.dto.UpdateAccount;
import com.example.entity.CustomerAddressEntity;
import com.example.entity.CustomerEntity;
import com.example.entity.VerificationToken;
import com.example.event.OnRegistrationCompleteEvent;
import com.example.service.ICustomerService;
import com.example.validator.RegisterFormValidator;
import com.sun.jndi.cosnaming.IiopUrl.Address;

@Controller
@RequestMapping(value = "/customer/account")
@SessionAttributes("customer")
public class CustomerController {

	@Autowired
	private ICustomerService customerService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private RegisterFormValidator validator;
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	private Map<String, String> countryList;

	private Map<String, String> regionList;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final int PASSWORD_LENGTH = 6;
	private Pattern pattern = Pattern.compile(EMAIL_PATTERN);

	@RequestMapping(value="/", method = RequestMethod.GET)
	public String showDashboard(Model model, HttpSession session){
		CustomerEntity customer = (CustomerEntity) session.getAttribute("customer");
		CustomerAddressEntity defaultBilling = null;
		CustomerAddressEntity defaultShipping = null;
		if (customer.getDefaultBilling() != null) {
			defaultBilling = customerService.getCustomerAddress(new Integer(
					customer.getDefaultBilling()));
		}
		if (customer.getDefaultShipping() != null) {
			defaultShipping = customerService.getCustomerAddress(new Integer(
					customer.getDefaultShipping()));
		}
		model.addAttribute("defaultBilling", defaultBilling);
		model.addAttribute("defaultShipping", defaultShipping);
		return "dashboard";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLogin() {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("login[username]") String email,
			@RequestParam("login[password]") String password, Model model)
			throws NoSuchAlgorithmException {
		if (email == null || "".equals(email)) {
			model.addAttribute("email",
					messageSource.getMessage("common.required", null, null));
			return "login";
		} else if (password == null || "".equals(password)) {
			model.addAttribute("password",
					messageSource.getMessage("common.required", null, null));
			return "login";
		} else if (!pattern.matcher(email).matches()) {
			model.addAttribute("email", messageSource.getMessage(
					"common.validation.email", null, null));
			return "login";
		} else if (password.length() < PASSWORD_LENGTH) {
			model.addAttribute("password", messageSource.getMessage(
					"common.validation.password", null, null));
			return "login";
		} else {
			CustomerEntity customer = customerService.getCustomer(email,
					password);
			if (customer == null) {
				model.addAttribute("error_login", messageSource.getMessage(
						"customer.account.login.error", null, null));
				return "login";
			} else {
				customer.setLogdate(new Date());
				customer.setLognum((short) (customer.getLognum() + 1));
				customerService.update(customer);
				model.addAttribute("customer", customer);
				return "about";
			}
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegister(Model model) {
		model.addAttribute("account", new RegisterModel());
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(
			@ModelAttribute("account") @Validated RegisterModel account,
			BindingResult result, Model model, WebRequest request)
			throws NoSuchAlgorithmException {
		if (result.hasErrors()) {
			return "register";
		} else {
			CustomerEntity customer = customerService.register(account);
			if (customer == null) {
				model.addAttribute("error_register", messageSource.getMessage(
						"customer.account.register.error.E1", null, null));
				return "register";
			} else {
				String appUrl = request.getContextPath();
				eventPublisher.publishEvent(new OnRegistrationCompleteEvent(
						this, customer, request.getLocale(), appUrl));
				return "register";
			}
		}
	}

	@RequestMapping(value = "/regitrationConfirm", method = RequestMethod.GET)
	public String confirmRegistration(WebRequest request, Model model,
			@RequestParam("token") String token) {
		Locale locale = request.getLocale();
		VerificationToken verificationToken = customerService
				.getVerificationToken(token);
		if (verificationToken == null) {
			model.addAttribute("message", "Not token");
			return "redirect:/bad-user.html?lang=" + locale.getLanguage();
		}

		CustomerEntity customer = verificationToken.getCustomerEntity();
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime()
				.getTime()) <= 0) {
			model.addAttribute("message", "expired token");
			return "redirect:/bad-user.html?lang=" + locale.getLanguage();
		}
		customer.setIsActive((short) 1);
		customerService.update(customer);
		return "redirect:/login?lang=" + request.getLocale().getLanguage();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String showUpdateAccount(Model model, HttpSession session,
			@RequestParam(value = "changepass", required = false) Integer changePassword) {
		CustomerEntity customer = (CustomerEntity) session
				.getAttribute("customer");
		if (customer != null) {
			UpdateAccount update = new UpdateAccount();
			if(changePassword != null)
				update.setChangePassword(true);
			model.addAttribute("update", update);
			return "update-account";
		}
		return "login";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String updateAccount(
			@ModelAttribute("update") @Validated UpdateAccount account,
			Model model, BindingResult result, HttpSession session)
			throws NoSuchAlgorithmException {
		if (result.hasErrors()) {
			return "update-account";
		} else {
			CustomerEntity customer = (CustomerEntity) session
					.getAttribute("customer");
			customer.setFirstname(account.getFirstName());
			customer.setLastname(account.getLastName());
			if (account.isChangePassword()) {
				if (customer.getPassword().equals(
						customerService.hashPassword(account
								.getCurrentPassword()))) {
					customer.setPassword(customerService.hashPassword(account
							.getPassword()));
				} else {
					model.addAttribute("error_update", "Sai passs");
				}
			}
			customerService.update(customer);
			model.addAttribute("message_update", "da update");
			return "update-account";
		}
	}

	@RequestMapping(value = "/address", method = RequestMethod.GET)
	public String showAddress(Model model, HttpSession session) {
		CustomerEntity customer = (CustomerEntity) session
				.getAttribute("customer");
		CustomerAddressEntity defaultBilling = null;
		CustomerAddressEntity defaultShipping = null;
		if (customer.getDefaultBilling() != null) {
			defaultBilling = customerService.getCustomerAddress(new Integer(
					customer.getDefaultBilling()));
		}
		if (customer.getDefaultShipping() != null) {
			defaultShipping = customerService.getCustomerAddress(new Integer(
					customer.getDefaultShipping()));
		}
		List<CustomerAddressEntity> listAddress = customerService
				.findAdditionalAddress(customer.getEntityId(),
						customer.getDefaultBilling(),
						customer.getDefaultShipping());
		model.addAttribute("defaultBilling", defaultBilling);
		model.addAttribute("defaultShipping", defaultShipping);
		model.addAttribute("listAddress", listAddress);
		return "address";
	}

	@RequestMapping(value = "/address/new", method = RequestMethod.GET)
	public String showNewAddress(Model model) {
		countryList = new HashMap<String, String>();
		countryList.put("VN", "Viet Nam");
		regionList = new HashMap<String, String>();
		regionList.put("1", "Ho Chi Minh");
		regionList.put("2", "Ha Noi");
		model.addAttribute("address", new AddressAccount());
		model.addAttribute("countryList", countryList);
		model.addAttribute("regionList", regionList);
		return "update-address";
	}

	@RequestMapping(value = "/address/new", method = RequestMethod.POST)
	public String addNewAddress(
			@ModelAttribute("address") @Validated AddressAccount address,
			Model model, HttpSession session, BindingResult result) {
		if (result.hasErrors()) {
			return "update-address";
		} else {
			CustomerEntity customer = (CustomerEntity) session
					.getAttribute("customer");
			address.setRegion(regionList.get(address.getRegionId()));
			address.setCountry(countryList.get(address.getCountryId()));
			customerService.saveAdress(address, customer);
		}
		model.addAttribute("message_address", "Cap nhat thanh cong");
		return "update-address";
	}

	@RequestMapping(value = "/address/edit/id/{entityId}", method = RequestMethod.GET)
	public String showUpdateAddress(Model model, @PathVariable("entityId") Integer id,
			HttpSession session) {
		CustomerAddressEntity cusAddress = customerService.getCustomerAddress(id);
		CustomerEntity customer = (CustomerEntity) session.getAttribute("customer");
		if(cusAddress == null){
			return "edit-address";
		} else{
			AddressAccount address = new AddressAccount();
			address.setEntityId(cusAddress.getEntityId());
			address.setFirstName(cusAddress.getFirstname());
			address.setLastName(cusAddress.getLastname());
			address.setTelephone(cusAddress.getPhone());
			address.setStreet(cusAddress.getStreet());
			address.setRegionId(cusAddress.getRegionId());
			address.setCountryId(cusAddress.getCountryId());
			countryList = new HashMap<String, String>();
			countryList.put("VN", "Viet Nam");
			regionList = new HashMap<String, String>();
			regionList.put("1", "Ho Chi Minh");
			regionList.put("2", "Ha Noi");
			model.addAttribute("address", address);
			model.addAttribute("defaultBilling", customer.getDefaultBilling());
			model.addAttribute("defaultShipping", customer.getDefaultShipping());
			model.addAttribute("countryList", countryList);
			model.addAttribute("regionList", regionList);
			return "edit-address";
		}
	}
	
	@RequestMapping(value = "/address/edit/id/{entityId}", method = RequestMethod.POST)
	public String updateAddress(@ModelAttribute("address") @Validated AddressAccount address,
			Model model, HttpSession session, BindingResult result) {
		if (result.hasErrors()) {
			return "update-address";
		} else {
			CustomerEntity customer = (CustomerEntity) session
				.getAttribute("customer");
			address.setRegion(regionList.get(address.getRegionId()));
			address.setCountry(countryList.get(address.getCountryId()));
			customerService.updateAdress(address, customer);
		}
		model.addAttribute("message_address", "Cap nhat thanh cong");
		return "address";
	}
	
	@RequestMapping(value = "/address/delete/id/{entityId}", method = RequestMethod.GET)
	public String deleteAddress(Model model, @PathVariable("entityId") Integer id) {
		if(id == null){
			return "address";
		}
		customerService.deleteCustomerAddress(id);
		return "address";
	}

}
