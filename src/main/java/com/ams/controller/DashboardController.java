package com.ams.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ams.dto.UserResponseDTO;
import com.ams.service.DashboardService;

@RestController
@CrossOrigin(
	    origins = "https://attendance-tracker-app.onrender.com",
	    allowedHeaders = "*",
	    methods = {RequestMethod.POST, RequestMethod.OPTIONS}
	)
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;
	
	@GetMapping("/fetch")
	public Map<String, Object> fetch(){
		return dashboardService.fetch();
	}
}
