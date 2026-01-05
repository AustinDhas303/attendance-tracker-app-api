package com.ams.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ams.dto.StudentDTO;
import com.ams.dto.StudentResponseDTO;
import com.ams.dto.UserResponseDTO;
import com.ams.model.RollSequence;
import com.ams.model.Student;
import com.ams.repository.RollSequenceRepository;
import com.ams.repository.StudentRepository;
import com.ams.repository.UserRepository;
import com.ams.model.User;

import jakarta.transaction.Transactional;

@Service
public class StudentService {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private RollSequenceRepository rollSequenceRepository;
	
	@Autowired
	private UserRepository userRepository;
	
 	public Map<String, String> createStudent(StudentDTO studentDTO) {
		// TODO Auto-generated method stub
 		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = userDetails.getUsername();
		User user = userRepository.getEmail(email);
		Integer roleId = user.getRole().getRoleId();
		Map<String, String> map = new HashMap<String, String>();
		if(roleId == 1) {
			Student student = new Student();
			Integer rollNo = getNextRollNumber();
			String studentName = studentDTO.getStudentName();
			System.out.println("Student name: "+studentName);
			String department = studentDTO.getDepartment();
			System.out.println("Department: "+department);
			student.setRollNo(rollNo);
			student.setStudentName(studentName);
			student.setDepartment(department);
			studentRepository.save(student);
			map.put("status", "success");
			map.put("message", "Student created successfully");
		}
		else {
			map.put("message", "Only Admin can create category");
			return map;
		}
		return map;
	}

	public Map<String, String> updateStudent(StudentDTO studentDTO) {
		// TODO Auto-generated method stub
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = userDetails.getUsername();
		User user = userRepository.getEmail(email);
		Integer roleId = user.getRole().getRoleId();
		Map<String, String> map = new HashMap<String, String>();
		Integer studentId = studentDTO.getStudentId();
		Student student = studentRepository.getStudent(studentId);
		if(roleId == 1) {
			if(student == null) {
				map.put("status", "failure");
				map.put("message", "Student is not found");
			}else {
				student.setRollNo(studentDTO.getRollNo());
				student.setStudentName(studentDTO.getStudentName());
				student.setDepartment(studentDTO.getDepartment());
				studentRepository.save(student);
				map.put("status", "success");
				map.put("message", "Student updated successfully");
			}
		}else {
			map.put("message", "Only Admin can update student");
			return map;
		}
		return map;
	}

	public Map<String, String> deleteStudent(Integer studentId) {
		// TODO Auto-generated method stub
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = userDetails.getUsername();
		User user = userRepository.getEmail(email);
		Integer roleId = user.getRole().getRoleId();
		Map<String, String> map = new HashMap<String, String>();
		Student student = studentRepository.getStudent(studentId);
		if(roleId == 1) {
			if(student == null) {
				map.put("status", "failure");
				map.put("message", "Student is not found");
			}else {
				studentRepository.delete(student);
				map.put("status", "success");
				map.put("message", "Student deleted successfully");
			}
		}else {
			map.put("message", "Only Admin can delete student");
			return map;
		}
		return map;
	}

	public StudentResponseDTO getAllStudents(int page, int size, String studentName) {
		// TODO Auto-generated method stub
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = userDetails.getUsername();
		User user = userRepository.getEmail(email);
		Integer roleId = user.getRole().getRoleId();
		StudentResponseDTO studentResponseDTO = new StudentResponseDTO();
		int pag = page;
		int siz = size;
		Pageable pageable = PageRequest.of(pag, siz);
		List<Student> students = studentRepository.getAllStudent(pageable, studentName);
		List<StudentDTO> studentDTOs = new ArrayList<StudentDTO>();
		if(roleId == 1 || roleId == 2) {
			for(Student s: students) {
				StudentDTO studentDTO = new StudentDTO();
				studentDTO.setStudentId(s.getStudentId());
				studentDTO.setRollNo(s.getRollNo());
				studentDTO.setStudentName(s.getStudentName());
				studentDTO.setDepartment(s.getDepartment());
				studentDTOs.add(studentDTO);
			}
			studentResponseDTO.setStudentDTOs(studentDTOs);
		}

		return studentResponseDTO;
	}
	
	@Transactional
	public Integer getNextRollNumber() {
	    RollSequence seq = rollSequenceRepository.findById(1).get();
	    Integer next = seq.getLastNumber() + 1;
	    seq.setLastNumber(next);
	    rollSequenceRepository.save(seq);
	    return next;
	}

}
