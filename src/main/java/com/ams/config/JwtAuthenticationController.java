package com.ams.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ams.dto.LoginDTO;
import com.ams.service.UserService;

@RestController
@RequestMapping("/api/v1/jwt")
public class JwtAuthenticationController {

	@Autowired 
	private UserService userService;
	
	@PostMapping("/login")
	public Map<String, Object> login(@RequestBody LoginDTO loginDTO) {
	   return userService.login(loginDTO.getEmailId(), loginDTO.getPassword());
	}
}
