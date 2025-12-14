package com.ams.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ams.dto.AttendanceDTO;
import com.ams.dto.AttendanceResponseDTO;
import com.ams.service.AttendanceService;

@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {
	
	@Autowired
	private AttendanceService attendanceService;

	@PostMapping("/create")
	public Map<String, String> takeAttendance(@RequestBody AttendanceDTO attendanceDTO){
		return attendanceService.takeAttendance(attendanceDTO);
	}
	
	@PutMapping("/update")
	public Map<String, String> updateAttendance(@RequestBody AttendanceDTO attendanceDTO){
		return attendanceService.updateAttendance(attendanceDTO);
	}
	
	@DeleteMapping("/delete/{attendanceId}")
    public Map<String, String> deleteAttendance(@PathVariable Integer attendanceId){
		return attendanceService.deleteAttendance(attendanceId);
	}
	
	@GetMapping("/fetch")
	public AttendanceResponseDTO fetchAttendance(@RequestParam int page, @RequestParam int size, @RequestParam String studentName){
		return attendanceService.fetchAttendance(page, size, studentName);
	}
}
