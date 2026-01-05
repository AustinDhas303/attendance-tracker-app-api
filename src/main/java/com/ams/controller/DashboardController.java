package com.ams.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ams.dto.UserResponseDTO;
import com.ams.service.DashboardService;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;
	
	@GetMapping("/fetch")
	public Map<String, Object> fetch(){
		return dashboardService.fetch();
	}
}
