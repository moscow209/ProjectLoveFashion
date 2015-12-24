package com.example.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.dao.DataService;
import com.example.model.Pakage;
import com.example.model.User;


@Controller
@RequestMapping("/api/users")
public class RestController {

	
	// http://localhost:8080/RestAPI/oauth/token?grant_type=password&client_id=restapp&client_secret=restapp&username=moskva209&password=123456
	// http://localhost:8080/RestAPI/api/users/?access_token=83aa500c-1c3a-46ad-b264-65b919157fdf
	
	@Autowired
	DataService dataService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public User list(){
		User a = new User(10, "tran", "minh", "vuong");
		return a;
	}
	
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@ResponseBody
	public String login( HttpSession session, HttpServletResponse response, Model model) {
		Pakage pakage = new Pakage("100", "shoper");
		System.out.print(pakage.getMoney());
		session.setAttribute("pakage", pakage);
		
		
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		try {
			httpResponse.sendRedirect("../../../login.jsp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return "redirect:../../../login.jsp";
	}
	
	@RequestMapping(value = "/check")
	public String  CheckLogin(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		String name = (String)request.getParameter("name");
		String password = (String)request.getParameter("password");
		Pakage pakage = (Pakage)session.getAttribute("pakage");
		
		System.out.print(pakage.getMoney());
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		int flag=0;
		List<User> list = dataService.getUserList();
		for(int i=0; i<list.size(); i++)
		{
			if((list.get(i).getName().equals(name)) && (list.get(i).getPassword().equals(password)))
			{
				flag=1;
				
			}
			
		}
		if(flag==1)
		{
			User user_Shoper = GetUserFromName(pakage.getAccountShoper());
			User user_Customer = GetUserFromName(name);
			int result = user_Customer.GiveMoney(user_Shoper, Float.parseFloat(pakage.getMoney()));
			
			switch(result)
			{
			case 0:		// Tai khoan khach hang khong du tien de tra.
			{
				break;
			}
			case 1:		// Tai khoan khach hang du tien de tra.
			{
				break;
			}
			}
			//
			try {
				httpResponse.sendRedirect("../../success.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			try {
				httpResponse.sendRedirect("../../login.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return name + password;
	}
	
	protected User GetUserFromName(String name)
	{
		List<User> list = dataService.getUserList();
		for(int i=0; i<list.size(); i++)
		{
			if((list.get(i).getName().equals(name)))
			{
				return list.get(i);				
			}
			
		}
		return null;
	}
}
