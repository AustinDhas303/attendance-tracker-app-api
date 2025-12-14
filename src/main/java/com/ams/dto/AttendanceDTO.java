package com.ams.dto;

import java.time.LocalDateTime;

import com.ams.model.Student;
import com.ams.util.AttendanceStatus;

import lombok.Data;

@Data
public class AttendanceDTO {
	
	private Integer attendanceId;
	private int periodNo;
	private LocalDateTime date;
	private AttendanceStatus attendanceStatus;
	private Student student;

}
