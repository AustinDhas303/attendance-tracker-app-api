package com.ams.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ams.model.Student;
import com.ams.model.User;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>{

	@Query("Select s from Student s where s.studentId=:studentId")
	Student getStudent(Integer studentId);

	@Query("SELECT s FROM Student s WHERE (:student IS NULL OR LOWER(s.studentName) LIKE LOWER(CONCAT(:student, '%'))) ORDER BY s.studentName DESC")
	List<Student> getAllStudent(Pageable pageable, String student);


}
