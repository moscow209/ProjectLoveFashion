package com.example.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.User;
/**
 * @author Nagesh.Chauhan
 *
 */
@Service
public class DataServiceImpl implements DataService {

	public List<User> getUserList() {
		
		// preparing user list with few hard coded values
		List<User> userList = new ArrayList<User>();
		
		userList.add(new User(1, "shoper", "shoper", "1000"));
		userList.add(new User(2, "customer", "customer", "1000"));
		
		return userList;
	}

}
