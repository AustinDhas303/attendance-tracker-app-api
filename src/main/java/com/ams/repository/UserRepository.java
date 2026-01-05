package com.ams.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ams.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmailId(String emailId);
	Optional<User> findByPassword(String password);

	@Query("select Count(u) from User u WHERE u.emailId=:emailId")
	int countByEmailId(@Param("emailId") String emailId);
	    
	@Query("select Count(u) from User u WHERE u.contactNo=:mobile")
	int countByMobile(@Param("mobile") String mobile);
	    
	@Query("Select u from User u where u.emailId =:emailId")
	User getEmail(@Param("emailId") String emailId);
	
	@Query("SELECT u FROM User u WHERE (:user1 IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT(:user1, '%')) OR "
			+ "LOWER(u.lastName) LIKE LOWER(CONCAT(:user1, '%'))) ORDER BY u.created_At DESC")
	List<User> getAllUsers(Pageable pageable, @Param("user1") String user1);
	
	@Query("Select u from User u where u.userId=:userId")
	User getUser(@Param("userId") Long userId);
	
	@Query("select count(u) from User u where u.role.roleId = 2")
	Integer getTotalTeacher();
	
}
