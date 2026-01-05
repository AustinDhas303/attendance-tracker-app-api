package com.ams.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.ams.util.AttendanceStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DailyAttendanceDTO {

	private Integer studentId;
    private String studentName;
    private Integer rollNo;
    private String department;
//    @JsonFormat(pattern = "dd-MM-yyyy")
//    private LocalDate date;
//
//    private Map<Integer, AttendanceStatus> periodStatus;
    private double attendancePercentage;
    private List<DailyAttendanceReportDTO> dailyAttendance;
}
