package com.ams.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ams.model.Attendance;
import com.ams.model.Student;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer>{

	Optional<Attendance> findByStudentAndDateAndPeriodNo(Student student, LocalDateTime attendanceDate, int periodNo);

	@Query("SELECT a FROM Attendance a right join a.student s WHERE (:studentName IS NULL OR LOWER(s.studentName) LIKE LOWER(CONCAT(:studentName, '%'))) "
			+ " AND (:attendanceStatus IS NULL OR LOWER(a.attendanceStatus) LIKE LOWER(CONCAT(:attendanceStatus, '%'))) ORDER BY a.date DESC")
	List<Attendance> fetch(Pageable pageable, String studentName, String attendanceStatus);

	@Query("Select a FROM Attendance a where a.attendanceId=:attendanceId")
	Attendance findByStudentId(Integer attendanceId);

	@Query("Select a FROM Attendance a right join a.student s where (:studentName IS NULL OR LOWER(s.studentName) LIKE LOWER(CONCAT(:studentName, '%'))) "
			+ "	AND (:attendanceStatus IS NULL OR LOWER(a.attendanceStatus) LIKE LOWER(CONCAT(:attendanceStatus, '%')))"
			+ " AND (:department IS NULL OR LOWER(s.department) LIKE LOWER(CONCAT(:department, '%'))) ORDER BY a.date DESC")
	List<Attendance> getAttendanceReport(Pageable pageable, String studentName, String attendanceStatus, String department);

	@Query("Select s.department, COUNT(DISTINCT s.studentId) from Attendance a right join a.student s "
			+ "group by s.department")
	List<Object[]> departmentCount();

	@Query("SELECT a FROM Attendance a JOIN a.student s " +
		       "WHERE a.date BETWEEN :startOfDay AND :endOfDay " +
		       "AND (:studentName IS NULL OR LOWER(s.studentName) LIKE LOWER(CONCAT(:studentName, '%'))) " +
		       "AND (:department IS NULL OR LOWER(s.department) like LOWER(concat(:department, '%'))) " +
		       "ORDER BY s.studentId, a.periodNo")
		List<Attendance> getTodayAttendance(
		        @Param("startOfDay") LocalDateTime startOfDay,
		        @Param("endOfDay") LocalDateTime endOfDay,
		        @Param("studentName") String studentName,
		        @Param("department") String department
		);

	@Query("""
			SELECT a FROM Attendance a
			JOIN a.student s
			WHERE s.studentId = :studentId
			  AND MONTH(a.date) = :month
			  AND YEAR(a.date) = :year
			""")
	List<Attendance> getMonthlyAttendance(Integer studentId, int month, int year);

	@Query("""
			SELECT a FROM Attendance a
			JOIN a.student s
			WHERE a.date >= :startDate
			  AND a.date < :endDate
			ORDER BY s.studentId
			""")
			List<Attendance> getMonthlyAllAttendance(
			        @Param("startDate") LocalDateTime startDate,
			        @Param("endDate") LocalDateTime endDate
			);

	@Query("""
			SELECT a FROM Attendance a
			JOIN a.student s
			WHERE a.date >= :startDate
			  AND a.date < :endDate
			  AND (:department IS NULL OR LOWER(s.department) like LOWER(concat(:department, '%')))
			ORDER BY s.studentId, a.date, a.periodNo
			""")
	List<Attendance> getMonthlyAttendanceDetails(@Param("startDate") LocalDateTime startDate,
	        @Param("endDate") LocalDateTime endDate, String department);

	@Query("""
			SELECT a FROM Attendance a
			WHERE a.date >= :start
			  AND a.date < :end
			""")
	List<Attendance> getTodayAttendancePer(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("""
			SELECT a.student.studentId
			FROM Attendance a
			WHERE a.date >= :monthStart
			  AND a.date < :monthEnd
			GROUP BY a.student.studentId
			HAVING
			(SUM(CASE WHEN a.attendanceStatus = 'PRESENT' THEN 1 ELSE 0 END) * 100.0
			 / COUNT(a.attendanceId)) < 75
			""")
			List<Integer> getStudentsBelowPercentage(
			        @Param("monthStart") LocalDateTime monthStart,
			        @Param("monthEnd") LocalDateTime monthEnd
			);

	@Query("""
			SELECT s.department,
			       SUM(CASE WHEN a.attendanceStatus = 'PRESENT' THEN 1 ELSE 0 END),
			       COUNT(a.attendanceId)
			FROM Attendance a
			JOIN a.student s
			WHERE a.date >= :start
			  AND a.date < :end
			GROUP BY s.department
			""")
	List<Object[]> getDepartmentWiseTodayAttendance(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);



}
