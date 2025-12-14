package com.ams.dto;

import java.util.List;

import com.ams.dto.UserDTO;

import lombok.Data;

@Data
public class UserResponseDTO {
	
	private List<UserDTO> userDTO;

}
