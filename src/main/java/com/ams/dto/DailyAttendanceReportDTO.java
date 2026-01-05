package com.ams.dto;

import java.time.LocalDate;
import java.util.Map;

import com.ams.util.AttendanceStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class DailyAttendanceReportDTO {

	@JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    private Map<Integer, AttendanceStatus> periodStatus;
    
}
