package com.ams.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ams.dto.AttendanceDTO;
import com.ams.dto.AttendanceResponseDTO;
import com.ams.dto.StudentDTO;
import com.ams.model.Attendance;
import com.ams.model.Student;
import com.ams.repository.AttendanceRepository;
import com.ams.repository.StudentRepository;

@Service
public class AttendanceService {
	
	@Autowired
	private AttendanceRepository attendanceRepository;
	
	@Autowired
	private StudentRepository studentRepository;

	public Map<String, String> takeAttendance(AttendanceDTO attendanceDTO) {
		// TODO Auto-generated method stub
		Map<String, String> response = new HashMap<>();

	    LocalDateTime attendanceDate = LocalDateTime.now();

	    Optional<Attendance> existingAttendance =
	            attendanceRepository.findByStudentAndDateAndPeriodNo(
	                    attendanceDTO.getStudent(),
	                    attendanceDate,
	                    attendanceDTO.getPeriodNo()
	            );

	    Attendance attendance;

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
	    return response;
	}

	public Map<String, String> updateAttendance(AttendanceDTO attendanceDTO) {
		// TODO Auto-generated method stub
		Map<String, String> response = new HashMap<>();

	    LocalDateTime date = LocalDateTime.now();

	    Attendance attendance = attendanceRepository.findByStudentId(attendanceDTO.getAttendanceId());

	    attendance.setPeriodNo(attendanceDTO.getPeriodNo());
	    attendance.setDate(date);
	    attendance.setAttendanceStatus(attendanceDTO.getAttendanceStatus());
	    attendanceRepository.save(attendance);

	    response.put("message", "Attendance updated successfully");
	    return response;
	}

	public Map<String, String> deleteAttendance(Integer attendanceId) {
		// TODO Auto-generated method stub
		Map<String, String> response = new HashMap<>();

	    if (!attendanceRepository.existsById(attendanceId)) {
	        throw new RuntimeException("Attendance not found");
	    }

	    attendanceRepository.deleteById(attendanceId);
	    response.put("message", "Attendance deleted successfully");

	    return response;
	}

	public AttendanceResponseDTO fetchAttendance(int page, int size, String studentName) {
		// TODO Auto-generated method stub
		AttendanceResponseDTO attendanceResponseDTO = new AttendanceResponseDTO();
		int pag = page;
		int siz = size;
		Pageable pageable = PageRequest.of(pag, siz);
		List<Attendance> attendances = attendanceRepository.fetch(pageable, studentName);
		List<AttendanceDTO> attendanceDTOs = new ArrayList<AttendanceDTO>();
		for(Attendance a: attendances) {
			AttendanceDTO attendance = new AttendanceDTO();
			attendance.setAttendanceId(a.getAttendanceId());
			attendance.setPeriodNo(a.getPeriodNo());
			attendance.setAttendanceStatus(a.getAttendanceStatus());
			attendance.setDate(LocalDateTime.now());
			attendance.setStudent(a.getStudent());
			attendanceDTOs.add(attendance);
		}
		attendanceResponseDTO.setAttendanceDTOs(attendanceDTOs);
		return attendanceResponseDTO;
	}

}
