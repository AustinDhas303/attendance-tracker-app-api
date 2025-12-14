package com.ams.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ams.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	
}
