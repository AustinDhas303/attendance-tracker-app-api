package com.ams.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ams.model.Attendance;
import com.ams.model.Student;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer>{

	Optional<Attendance> findByStudentAndDateAndPeriodNo(Student student, LocalDateTime attendanceDate, int periodNo);

	@Query("SELECT a FROM Attendance a join a.student s WHERE (:studentName IS NULL OR LOWER(s.studentName) LIKE LOWER(CONCAT(:studentName, '%'))) ORDER BY a.date DESC")
	List<Attendance> fetch(Pageable pageable, String studentName);

	@Query("Select a FROM Attendance a where a.attendanceId=:attendanceId")
	Attendance findByStudentId(Integer attendanceId);

}
