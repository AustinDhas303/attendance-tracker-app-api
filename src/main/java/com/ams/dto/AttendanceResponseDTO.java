package com.ams.dto;

import java.util.List;

import lombok.Data;

@Data
public class AttendanceResponseDTO {

	private List<AttendanceDTO> attendanceDTOs;
}
