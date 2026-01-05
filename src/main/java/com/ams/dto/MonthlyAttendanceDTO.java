package com.ams.dto;

import lombok.Data;

@Data
public class MonthlyAttendanceDTO {

	private Integer studentId;
    private String studentName;
    private int month;
    private int year;
    private double attendancePercentage;
    
}
