package com.example.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.example.dto.RegisterModel;
import com.example.entity.CustomerEntity;
import com.example.entity.VerificationToken;
import com.example.event.OnRegistrationCompleteEvent;
import com.example.service.ICustomerService;
import com.example.validator.RegisterFormValidator;

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

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final int PASSWORD_LENGTH = 6;
	private Pattern pattern = Pattern.compile(EMAIL_PATTERN);

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

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
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

}
