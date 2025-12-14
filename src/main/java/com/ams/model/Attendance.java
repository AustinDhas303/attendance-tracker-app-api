package com.ams.model;

import java.time.LocalDateTime;

import com.ams.util.AttendanceStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "attendance")
public class Attendance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer attendanceId;
	
	private LocalDateTime date;
	private int periodNo;
	@Enumerated(EnumType.STRING)
	private AttendanceStatus attendanceStatus;
	
	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;
}
