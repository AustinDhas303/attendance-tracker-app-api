package com.ams.dto;

import java.util.List;

import lombok.Data;

@Data
public class MonthlyAttendanceReportDTO {

	private Integer studentId;
	private Integer rollNo;
    private String studentName;
    private String department;
    private int month;
    private int year;
    private double monthlyPercentage;

    private List<DailyAttendanceReportDTO> dailyAttendance;
    
}
