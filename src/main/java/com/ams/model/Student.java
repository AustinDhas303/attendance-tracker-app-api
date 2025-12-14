package com.ams.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "student")
@Entity
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer studentId;
	
	private Integer rollNo;
	@Column(length = 40, nullable = false)
	private String studentName;
	@Column(length = 30, nullable = false)
	private String department;

}
