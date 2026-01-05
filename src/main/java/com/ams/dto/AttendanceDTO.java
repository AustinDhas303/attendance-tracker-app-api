package com.ams.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ams.model.Student;
import com.ams.util.AttendanceStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AttendanceDTO {
	
	private Integer attendanceId;
	private int periodNo;
//	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDateTime date;
	private AttendanceStatus attendanceStatus;
	private Student student;

}
