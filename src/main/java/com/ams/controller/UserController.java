package com.ams.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ams.dto.LoginDTO;
import com.ams.dto.UserDTO;
import com.ams.service.UserService;
import com.ams.dto.UserResponseDTO;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(
	    origins = "https://attendance-tracker-app.onrender.com",
	    allowedHeaders = "*",
	    methods = {RequestMethod.POST, RequestMethod.OPTIONS}
	)
public class UserController {
	
	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public Map<String, String> register(@RequestBody UserDTO userDTO) {
	   return userService.register(userDTO);
	}
	
	@DeleteMapping("/deleteuser/{userId}")
    public Map<String, String> deleteUser(@PathVariable Long userId){
    	return userService.deleteUser(userId);
    }

	@GetMapping("/fetchallusers")
	public UserResponseDTO getAllUsers(@RequestParam int page, @RequestParam int size, @RequestParam String userName){
		return userService.getAllUsers(page, size, userName);
	}
	
	@PutMapping("/updateUser")
	public Map<String, String> updateUser(@RequestBody UserDTO userDTO){
		return userService.updateUser(userDTO);
	}
	
	@PostMapping("/logout")
	public Object logout() {
		return userService.logout();
	}
}
