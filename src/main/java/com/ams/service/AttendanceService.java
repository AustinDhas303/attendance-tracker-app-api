package com.ams.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ams.dto.AttendanceDTO;
import com.ams.dto.AttendanceResponseDTO;
import com.ams.dto.StudentDTO;
import com.ams.model.Attendance;
import com.ams.model.Student;
import com.ams.model.User;
import com.ams.repository.AttendanceRepository;
import com.ams.repository.StudentRepository;
import com.ams.repository.UserRepository;

@Service
public class AttendanceService {
	
	@Autowired
	private AttendanceRepository attendanceRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private UserRepository userRepository;

	public Map<String, String> takeAttendance(AttendanceDTO attendanceDTO) {
		// TODO Auto-generated method stub
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = userDetails.getUsername();
		User user = userRepository.getEmail(email);
		Integer roleId = user.getRole().getRoleId();
		Map<String, String> response = new HashMap<>();

	    LocalDateTime attendanceDate = LocalDateTime.now();

	    Optional<Attendance> existingAttendance =
	            attendanceRepository.findByStudentAndDateAndPeriodNo(
	                    attendanceDTO.getStudent(),
	                    attendanceDate,
	                    attendanceDTO.getPeriodNo()
	            );

	    Attendance attendance;
		if(roleId == 2) {
		    if (existingAttendance.isPresent()) {
		        attendance = existingAttendance.get();
		        attendance.setAttendanceStatus(attendanceDTO.getAttendanceStatus());
		    } else {
		        attendance = new Attendance();
		        Integer studentId = attendanceDTO.getStudent().getStudentId();
		        Student student = studentRepository.findById(studentId)
		                .orElseThrow(() -> new RuntimeException("Student not found"));
		        attendance.setStudent(student);
		        attendance.setDate(attendanceDate);
		        attendance.setPeriodNo(attendanceDTO.getPeriodNo());
		        attendance.setAttendanceStatus(attendanceDTO.getAttendanceStatus());
		    }

		    attendanceRepository.save(attendance);

		    response.put("message", "Attendance marked successfully");			
		}else {
			response.put("message", "Only Teacher can mark attendance");
			return response;
		}

	    return response;
	}

	public Map<String, String> updateAttendance(AttendanceDTO attendanceDTO) {
		// TODO Auto-generated method stub
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = userDetails.getUsername();
		User user = userRepository.getEmail(email);
		Integer roleId = user.getRole().getRoleId();
		Map<String, String> response = new HashMap<>();

	    LocalDateTime date = LocalDateTime.now();

	    Attendance attendance = attendanceRepository.findByStudentId(attendanceDTO.getAttendanceId());
		if(roleId == 2) {
		    attendance.setPeriodNo(attendanceDTO.getPeriodNo());
		    attendance.setDate(date);
		    attendance.setAttendanceStatus(attendanceDTO.getAttendanceStatus());
		    attendanceRepository.save(attendance);

		    response.put("message", "Attendance updated successfully");			
		}else {
			response.put("message", "Only Teacher can update attendance");
			return response;
		}

	    return response;
	}

	public Map<String, String> deleteAttendance(Integer attendanceId) {
		// TODO Auto-generated method stub
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = userDetails.getUsername();
		User user = userRepository.getEmail(email);
		Integer roleId = user.getRole().getRoleId();
		Map<String, String> response = new HashMap<>();
		if(roleId == 2) {
		    if (!attendanceRepository.existsById(attendanceId)) {
		        throw new RuntimeException("Attendance not found");
		    }

		    attendanceRepository.deleteById(attendanceId);
		    response.put("message", "Attendance deleted successfully");			
		}else {
			response.put("message", "Only Teacher can delete attendance");
			return response;
		}
	    return response;
	}

	public AttendanceResponseDTO fetchAttendance(int page, int size, String studentName, String attendanceStatus) {
		// TODO Auto-generated method stub
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = userDetails.getUsername();
		User user = userRepository.getEmail(email);
		Integer roleId = user.getRole().getRoleId();
		AttendanceResponseDTO attendanceResponseDTO = new AttendanceResponseDTO();
		int pag = page;
		int siz = size;
		Pageable pageable = PageRequest.of(pag, siz);
		List<Attendance> attendances = attendanceRepository.fetch(pageable, studentName, attendanceStatus);
		List<AttendanceDTO> attendanceDTOs = new ArrayList<AttendanceDTO>();
		if(roleId == 2) {

			for(Attendance a: attendances) {
				AttendanceDTO attendance = new AttendanceDTO();
				attendance.setAttendanceId(a.getAttendanceId());
				attendance.setPeriodNo(a.getPeriodNo());
				attendance.setAttendanceStatus(a.getAttendanceStatus());
			    attendance.setDate(a.getDate());
				attendance.setStudent(a.getStudent());
				attendanceDTOs.add(attendance);
			}
			attendanceResponseDTO.setAttendanceDTOs(attendanceDTOs);	
		}
		return attendanceResponseDTO;
	}

}
