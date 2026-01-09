package com.ams.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ams.dto.AttendanceResponseDTO;
import com.ams.dto.DailyAttendanceDTO;
import com.ams.dto.MonthlyAttendanceDTO;
import com.ams.dto.MonthlyAttendanceReportDTO;
import com.ams.dto.UserDTO;
import com.ams.service.ReportService;

@RestController
@RequestMapping("/api/v1/report")
@CrossOrigin(
	    origins = "https://attendance-tracker-app.onrender.com",
	    allowedHeaders = "*",
	    methods = {RequestMethod.POST, RequestMethod.OPTIONS}
	)
public class ReportController {
	
	@Autowired
	private ReportService reportService;

	@GetMapping("/attendance")
	public AttendanceResponseDTO register(@RequestParam int page, @RequestParam int size, @RequestParam String studentName, @RequestParam String attendanceStatus, 
			@RequestParam String department) {
	   return reportService.attendanceReport(page, size, studentName, attendanceStatus, department);
	}
	
	@GetMapping("/attendance/today")
	public List<DailyAttendanceDTO> getTodayAttendance(
//			@RequestParam int page, @RequestParam int size,
	        @RequestParam(required = false) String studentName,
	        @RequestParam(required = false) String department
	) {
	    return reportService.getTodayAttendanceReport(studentName, department);
	}
	
	@GetMapping("/attendance/monthly-percentage")
	public double getMonthlyAttendancePercentage(
	        @RequestParam Integer studentId,
	        @RequestParam int month,
	        @RequestParam int year
	) {
	    return reportService.getMonthlyAttendancePercentage(
	            studentId, month, year
	    );
	}
	
	@GetMapping("/all-attendance/monthly-percentage")
	public List<MonthlyAttendanceDTO> getMonthlyAttendancePercentage(
	        @RequestParam int month,
	        @RequestParam int year
	) {
	    return reportService.getMonthlyAttendancePercentage(month, year);
	}
	
	@GetMapping("/attendance/monthly-report")
	public List<MonthlyAttendanceReportDTO> getMonthlyAttendanceReport(
//			@RequestParam int page,
//			@RequestParam int size,
	        @RequestParam int month,
	        @RequestParam int year,
	        @RequestParam(required = false) String department
	) {
	    return reportService.getMonthlyAttendanceReport(month, year, department);
	}



}
