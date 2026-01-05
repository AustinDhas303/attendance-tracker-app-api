package com.ams.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ams.dto.AttendanceDTO;
import com.ams.dto.AttendanceResponseDTO;
import com.ams.dto.DailyAttendanceDTO;
import com.ams.dto.DailyAttendanceReportDTO;
import com.ams.dto.MonthlyAttendanceDTO;
import com.ams.dto.MonthlyAttendanceReportDTO;
import com.ams.model.Attendance;
import com.ams.model.Student;
import com.ams.model.User;
import com.ams.repository.AttendanceRepository;
import com.ams.repository.UserRepository;
import com.ams.util.AttendanceStatus;

@Service
public class ReportService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AttendanceRepository attendanceRepository;
	
	private static final int TOTAL_PERIODS_PER_DAY = 8;
	
	private static final int TOTAL_DAYS_PER_MONTH = 22;

	public AttendanceResponseDTO attendanceReport(int page, int size, String studentName, String attendanceStatus,
			String department) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> map = new HashMap<String, Object>();
		String emailId = userDetails.getUsername();
		User user = userRepository.getEmail(emailId);
		Integer roleId = user.getRole().getRoleId();
		int pag = page;
		int siz = size;
		Pageable pageable = PageRequest.of(pag, siz);
		List<Attendance> attendances = attendanceRepository.getAttendanceReport(pageable, studentName, attendanceStatus, department);
		AttendanceResponseDTO attendanceResponseDTO = new AttendanceResponseDTO();
		List<AttendanceDTO> attendanceDTOs = new ArrayList<AttendanceDTO>();
//		LocalDateTime localDateTime = null;
//		DateTimeFormatter dateTimeFormatter = null;
 		if(roleId == 1) {
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

	public List<DailyAttendanceDTO> getTodayAttendanceReport(String studentName, String department) {
		// TODO Auto-generated method stub
		LocalDate today = LocalDate.now();

	    LocalDateTime startOfDay = today.atStartOfDay();
	    LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

	    List<Attendance> attendances =
	            attendanceRepository.getTodayAttendance(
	                    startOfDay,
	                    endOfDay,
	                    studentName,
	                    department
	            );

	    Map<Integer, DailyAttendanceDTO> studentMap = new LinkedHashMap<>();

	    for (Attendance a : attendances) {

	        Integer studentId = a.getStudent().getStudentId();

	        DailyAttendanceDTO studentDTO =
	                studentMap.computeIfAbsent(studentId, k -> {

	                    DailyAttendanceDTO dto = new DailyAttendanceDTO();
	                    dto.setStudentId(studentId);
	                    dto.setStudentName(a.getStudent().getStudentName());
	                    dto.setRollNo(a.getStudent().getRollNo());
	                    dto.setDepartment(a.getStudent().getDepartment());
	                    dto.setDailyAttendance(new ArrayList<>());   // IMPORTANT
	                    return dto;
	                });

	        DailyAttendanceReportDTO todayReport =
	                studentDTO.getDailyAttendance()
	                        .stream()
	                        .findFirst()
	                        .orElseGet(() -> {

	                            DailyAttendanceReportDTO d = new DailyAttendanceReportDTO();
	                            d.setDate(today);
	                            d.setPeriodStatus(new LinkedHashMap<>());

	                            studentDTO.getDailyAttendance().add(d);
	                            return d;
	                        });

	        todayReport.getPeriodStatus()
	                   .put(a.getPeriodNo(), a.getAttendanceStatus());
	    }

	    for (DailyAttendanceDTO student : studentMap.values()) {

	        DailyAttendanceReportDTO todayReport = student.getDailyAttendance().get(0);

	        for (int p = 1; p <= TOTAL_PERIODS_PER_DAY; p++) {
	            todayReport.getPeriodStatus()
	                       .putIfAbsent(p, AttendanceStatus.ABSENT);
	        }

	        long presentCount = todayReport.getPeriodStatus()
	                                       .values()
	                                       .stream()
	                                       .filter(x -> x == AttendanceStatus.PRESENT)
	                                       .count();

	        double percentage = (presentCount * 100.0) / TOTAL_PERIODS_PER_DAY;

	        student.setAttendancePercentage(
	                Math.round(percentage * 100.0) / 100.0
	        );
	    }

	    return new ArrayList<>(studentMap.values());

	}

	public double getMonthlyAttendancePercentage(Integer studentId, int month, int year) {
		 List<Attendance> attendances =
		            attendanceRepository.getMonthlyAttendance(
		                    studentId, month, year
		            );

		    long totalPeriods = attendances.size();
		    long presentCount = attendances.stream()
		            .filter(a -> a.getAttendanceStatus() == AttendanceStatus.PRESENT)
		            .count();

		    return totalPeriods == 0
		            ? 0
		            : Math.round((presentCount * 100.0 / totalPeriods) * 100.0) / 100.0;
	}

	public List<MonthlyAttendanceDTO> getMonthlyAttendancePercentage(int month, int year) {
		YearMonth yearMonth = YearMonth.of(year, month);

	    LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
	    LocalDateTime endDate = yearMonth.plusMonths(1).atDay(1).atStartOfDay();

	    List<Attendance> attendances =
	            attendanceRepository.getMonthlyAllAttendance(
	                    startDate, endDate
	            );

	    Map<Integer, List<Attendance>> studentMap =
	            attendances.stream()
	                    .collect(Collectors.groupingBy(
	                            a -> a.getStudent().getStudentId()
	                    ));

	    List<MonthlyAttendanceDTO> result = new ArrayList<>();

	    for (Map.Entry<Integer, List<Attendance>> entry : studentMap.entrySet()) {

	        Integer studentId = entry.getKey();
	        List<Attendance> records = entry.getValue();

	        long total = records.size();
	        long present = records.stream()
	                .filter(a -> a.getAttendanceStatus() == AttendanceStatus.PRESENT)
	                .count();

	        double percentage = total == 0
	                ? 0
	                : (present * 100.0) / total;

	        Student student = records.get(0).getStudent();

	        MonthlyAttendanceDTO dto = new MonthlyAttendanceDTO();
	        dto.setStudentId(studentId);
	        dto.setStudentName(student.getStudentName());
	        dto.setMonth(month);
	        dto.setYear(year);
	        dto.setAttendancePercentage(
	                Math.round(percentage * 100.0) / 100.0
	        );

	        result.add(dto);
	    }

	    return result;
	}

	public List<MonthlyAttendanceReportDTO> getMonthlyAttendanceReport(int month, int year, String department) {
		YearMonth yearMonth = YearMonth.of(year, month);

	    LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
	    LocalDateTime endDate = yearMonth.plusMonths(1).atDay(1).atStartOfDay();


//	    int pag = page;
//		int siz = size;
//		Pageable pageable = PageRequest.of(pag, siz);
		
	    
	    List<Attendance> attendances =
	            attendanceRepository.getMonthlyAttendanceDetails(
	                    startDate, endDate, department
	            );

	    // studentId -> date -> period -> status
	    Map<Integer, Map<LocalDate, Map<Integer, AttendanceStatus>>> dataMap =
	            new LinkedHashMap<>();

	    // studentId -> Student (cache student info)
	    Map<Integer, Student> studentCache = new HashMap<>();

	    for (Attendance a : attendances) {

	        Integer studentId = a.getStudent().getStudentId();
	        LocalDate date = a.getDate().toLocalDate();

	        studentCache.putIfAbsent(studentId, a.getStudent());

	        dataMap
	            .computeIfAbsent(studentId, k -> new LinkedHashMap<>())
	            .computeIfAbsent(date, k -> new LinkedHashMap<>())
	            .put(a.getPeriodNo(), a.getAttendanceStatus());
	    }

	    List<MonthlyAttendanceReportDTO> result = new ArrayList<>();

	    for (Map.Entry<Integer, Map<LocalDate, Map<Integer, AttendanceStatus>>> studentEntry
	            : dataMap.entrySet()) {

	        Integer studentId = studentEntry.getKey();
	        Student student = studentCache.get(studentId);

	        List<DailyAttendanceReportDTO> dailyList = new ArrayList<>();

	        long totalPeriods = 0;
	        long presentPeriods = 0;

	        for (Map.Entry<LocalDate, Map<Integer, AttendanceStatus>> dateEntry
	                : studentEntry.getValue().entrySet()) {

	            LocalDate date = dateEntry.getKey();
	            Map<Integer, AttendanceStatus> periodMap = dateEntry.getValue();

	            // Fill missing periods as ABSENT
	            for (int p = 1; p <= TOTAL_PERIODS_PER_DAY; p++) {
	                periodMap.putIfAbsent(p, AttendanceStatus.ABSENT);
	            }

	            DailyAttendanceReportDTO daily = new DailyAttendanceReportDTO();
	            daily.setDate(date);
	            daily.setPeriodStatus(periodMap);

	            dailyList.add(daily);

	            totalPeriods += TOTAL_PERIODS_PER_DAY;

	            presentPeriods += periodMap.values()
	                    .stream()
	                    .filter(status -> status == AttendanceStatus.PRESENT)
	                    .count();
	        }

	        double monthlyPercentage = totalPeriods == 0
	                ? 0
	                : (presentPeriods * 100.0) / totalPeriods;

	        monthlyPercentage = Math.round(monthlyPercentage * 100.0) / 100.0;

	        MonthlyAttendanceReportDTO dto = new MonthlyAttendanceReportDTO();
	        dto.setStudentId(studentId);
	        dto.setRollNo(student.getRollNo());
	        dto.setDepartment(student.getDepartment());
	        dto.setStudentName(student.getStudentName());
	        dto.setMonth(month);
	        dto.setYear(year);
	        dto.setDailyAttendance(dailyList);
	        dto.setMonthlyPercentage(monthlyPercentage);

	        result.add(dto);
	    }
	    return result;
	}

	
}
