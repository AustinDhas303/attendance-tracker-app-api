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

import com.ams.dto.StudentDTO;
import com.ams.dto.StudentResponseDTO;
import com.ams.service.StudentService;

@RestController
@CrossOrigin(
	    origins = "https://attendance-tracker-app.onrender.com",
	    allowedHeaders = "*",
	    methods = {RequestMethod.POST, RequestMethod.OPTIONS}
	)
@RequestMapping("/api/v1/student")
public class StudentController {
	
	@Autowired
	private StudentService studentService;

	@PostMapping("/create")
	public Map<String, String> createStudent(@RequestBody StudentDTO studentDTO) {
	   return studentService.createStudent(studentDTO);
	}
	
	@PutMapping("/update")
	public Map<String, String> updateUser(@RequestBody StudentDTO studentDTO){
		return studentService.updateStudent(studentDTO);
	}
	
	@DeleteMapping("/delete/{studentId}")
    public Map<String, String> deleteStudent(@PathVariable Integer studentId){
    	return studentService.deleteStudent(studentId);
    }

	@GetMapping("/fetch")
	public StudentResponseDTO getAllStudent(@RequestParam int page, @RequestParam int size, @RequestParam String studentName){
		return studentService.getAllStudents(page, size, studentName);
	}
	
}
