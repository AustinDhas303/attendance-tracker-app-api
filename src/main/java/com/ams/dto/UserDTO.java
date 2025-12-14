package com.ams.dto;

import java.util.Date;

import com.ams.model.Role;

import lombok.Data;

@Data
public class UserDTO {
	
	private Long userId;
	private String firstName;
	private String lastName;
	private String password;
	private String emailId;
	private int status;
	private String contactNo;
	private String address;
	private Date created_At;
	private Date updated_At;
	private Role role;

}
