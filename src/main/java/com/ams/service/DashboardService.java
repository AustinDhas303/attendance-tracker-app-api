package com.ams.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ams.model.Attendance;
import com.ams.model.User;
import com.ams.repository.AttendanceRepository;
import com.ams.repository.StudentRepository;
import com.ams.repository.UserRepository;
import com.ams.util.AttendanceStatus;

@Service
public class DashboardService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AttendanceRepository attendanceRepository;
	
	@Autowired
	private StudentRepository studentRepository;

	public Map<String, Object> fetch() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailId = userDetails.getUsername();
		User user = userRepository.getEmail(emailId);
		Integer roleId = user.getRole().getRoleId();
//		int pag = page;
//		int siz = size;
//		Pageable pageable = PageRequest.of(pag, siz);
		
//		List<Map<String, Object>> list = new ArrayList<>();
		
		Map<String, Object> response = new LinkedHashMap<>();

		if (roleId == 1 || roleId == 2) {

		    Integer studentCount = studentRepository.getStudentCount();
		    Integer teacherCount = userRepository.getTotalTeacher();

//		    Map<String, Object> studentWrapper = new LinkedHashMap<>();
//		    Map<String, Object> studentMap = new LinkedHashMap<>();
//
//		    studentMap.put("totalStudents", studentCount);
//		    studentWrapper.put("student", studentMap);
//
//		    list.add(studentWrapper);
//
//		    List<Object[]> departmentCount = attendanceRepository.departmentCount();
//
//		    Map<String, Object> departmentWrapper = new LinkedHashMap<>();
//		    Map<String, Long> departmentMap = new LinkedHashMap<>();
//
//		    for (Object[] row : departmentCount) {
//		        String dept = (String) row[0];
//		        Long count = (Long) row[1];
//		        departmentMap.put(dept, count);
//		    }
//
//		    departmentWrapper.put("department", departmentMap);
//		    list.add(departmentWrapper);
//		}
		    
		    LocalDate today = LocalDate.now();
		    LocalDateTime start = today.atStartOfDay();
		    LocalDateTime end = today.plusDays(1).atStartOfDay();

		    List<Attendance> todayAttendance =
		            attendanceRepository.getTodayAttendancePer(start, end);

		    long present = todayAttendance.stream()
		            .filter(a -> a.getAttendanceStatus() == AttendanceStatus.PRESENT)
		            .count();

		    long total = todayAttendance.size();

		    double todayPercentage = total == 0 ? 0 : (present * 100.0) / total;

		    YearMonth currentMonth = YearMonth.now();
		    LocalDateTime monthStart =
		            currentMonth.atDay(1).atStartOfDay();
		    LocalDateTime monthEnd =
		            currentMonth.plusMonths(1).atDay(1).atStartOfDay();

		    int studentsBelow75 =
		            attendanceRepository
		                    .getStudentsBelowPercentage(monthStart, monthEnd)
		                    .size();

		    Map<String, Object> cards = new LinkedHashMap<>();
		    cards.put("totalStudents", studentCount);
		    cards.put("totalTeachers", teacherCount);
		    cards.put("todayAttendancePercentage",
		            Math.round(todayPercentage * 100.0) / 100.0);
		    cards.put("studentsBelow75", studentsBelow75);

		    response.put("cards", cards);

		    List<Object[]> deptData =
		            attendanceRepository.getDepartmentWiseTodayAttendance(start, end);

		    Map<String, Double> departmentAttendance = new LinkedHashMap<>();

		    for (Object[] row : deptData) {
		        String department = (String) row[0];
		        Long presentCount = (Long) row[1];
		        Long totalCount = (Long) row[2];

		        double deptPercentage =
		                totalCount == 0 ? 0 :
		                        (presentCount * 100.0) / totalCount;

		        departmentAttendance.put(
		                department,
		                Math.round(deptPercentage * 100.0) / 100.0
		        );
		    }

		    response.put("departmentAttendance", departmentAttendance);

		    Map<String, Long> todaySummary = new LinkedHashMap<>();
		    todaySummary.put("presentCount", present);
		    todaySummary.put("absentCount", total - present);

		    response.put("todaySummary", todaySummary);
		}
		    return response;

	}
}
