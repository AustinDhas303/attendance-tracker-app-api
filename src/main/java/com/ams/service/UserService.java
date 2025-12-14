package com.ams.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ams.dto.UserDTO;
import com.ams.dto.UserResponseDTO;
import com.ams.model.Role;
import com.ams.model.User;
import com.ams.repository.RoleRepository;
import com.ams.repository.UserRepository;
import com.ams.config.JwtUtil;
import com.ams.dto.UserResponseDTO;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
    private JwtUtil jwtUtil;
	
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public Map<String, String> register(UserDTO userDTO) {
		Map<String, String> map = new HashMap<String, String>();
		String email = userDTO.getEmailId();
		String mobile = userDTO.getContactNo();
		
		int emailCount = userRepository.countByEmailId(email);
		int mobileCount = userRepository.countByMobile(mobile);
	    
		if (emailCount > 0) {
			map.put("status", "error");
			map.put("message", "Email Id is Already Exist");
			return map;
		}
		if (mobileCount > 0) {
			map.put("status", "error");
			map.put("message", "Mobile Number is Already Exist");
			return map;
		}
		
		Integer roleId = userDTO.getRole().getRoleId();
		Role role = roleRepository.findById(roleId)
		               .orElseThrow(() -> new RuntimeException("Role not found"));
		
		String password = new BCryptPasswordEncoder().encode(userDTO.getPassword());
		User user = new User();
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setAddress(userDTO.getAddress());
		user.setContactNo(userDTO.getContactNo());
		user.setCreated_At(userDTO.getCreated_At());
		user.setEmailId(userDTO.getEmailId());
		user.setPassword(password);
		user.setRole(role);
		user.setStatus(userDTO.getStatus());
		user.setUpdated_At(userDTO.getUpdated_At());
		userRepository.save(user);
		map.put("status", "success");
		map.put("message", "User created successfully");
        return map;
	}

	public Map<String, String> deleteUser(Long userId) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailId = userDetails.getUsername();
		User u = userRepository.getEmail(emailId);
		Map<String, String> map = new HashMap<String, String>();
		if(u.getRole().getRoleId() == 1) {
			User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("user deatail not found :"+userId));
			userRepository.delete(user);
			map.put("message", "User deleted successfully");
		}else {
			map.put("error", "Only admin's allowed to delete user");
			return map;
		}
		return map;
	}

	public UserResponseDTO getAllUsers(int page, int size, String userName) {
		// TODO Auto-generated method stub
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailId = userDetails.getUsername();
		User user = userRepository.getEmail(emailId);
		UserResponseDTO responseDTO=new UserResponseDTO();
		if(user.getRole().getRoleId() == 1) {
			int pag = page;
			int siz = size;
			Pageable pageable = PageRequest.of(pag, siz);
			String user1 = userName;
			List<UserDTO> userDTOs=new ArrayList<>();
			List<User> users=userRepository.getAllUsers(pageable, user1);

			for(User u:users) {
				UserDTO userDTO=new UserDTO();
				userDTO.setUserId(u.getUserId());
				userDTO.setFirstName(u.getFirstName());
				userDTO.setLastName(u.getLastName());
				userDTO.setContactNo(u.getContactNo());
				userDTO.setStatus(u.getStatus());
				userDTO.setEmailId(u.getEmailId());
				userDTO.setAddress(u.getAddress());
				userDTO.setCreated_At(u.getCreated_At());
				userDTO.setUpdated_At(u.getUpdated_At());
				
				Integer roleId = u.getRole().getRoleId();
				Role role = roleRepository.findById(roleId)
				               .orElseThrow(() -> new RuntimeException("Role not found"));
				if (role != null) {
		            userDTO.setRole(role);
		        }
				userDTOs.add(userDTO);
			}
			responseDTO.setUserDTO(userDTOs);
		}
		
		return responseDTO;
	}

	public Map<String, String> updateUser(UserDTO userDTO) {
		// TODO Auto-generated method stub
		Map<String, String> response = new HashMap<>();

	    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    String emailId = userDetails.getUsername();
	    User loggedInUser = userRepository.getEmail(emailId);

	    if (loggedInUser == null) {
	        response.put("status", "fail");
	        response.put("message", "User not found in system.");
	        return response;
	    }

	    Long requestedUserId = userDTO.getUserId();

	    if (!loggedInUser.getUserId().equals(requestedUserId)) {
	        response.put("status", "unauthorized");
	        response.put("message", "You can only update your own profile.");
	        return response;
	    }

	    loggedInUser.setFirstName(userDTO.getFirstName());
	    loggedInUser.setLastName(userDTO.getLastName());
	    loggedInUser.setEmailId(userDTO.getEmailId());
	    loggedInUser.setContactNo(userDTO.getContactNo());
	    loggedInUser.setAddress(userDTO.getAddress());

	    userRepository.save(loggedInUser);

	    response.put("status", "success");
	    response.put("message", "Profile updated successfully.");
	    return response;
	}

	public Object logout() {
		// TODO Auto-generated method stub
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> map = new HashMap<String, Object>();
		String emailId = userDetails.getUsername();
		User user = userRepository.getEmail(emailId);
		map.put("status", "success");
		map.put("message", "User logged out successfully");
		return map;
	}

	public Map<String, Object> login(String emailId, String password) {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmailId(), user.getRole().getRoleName());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole().getRoleName()); // Return role
        response.put("status", "Login success");
//		response.put("roleId",user.getRole().getRoleId());
		response.put("userId",user.getUserId());

        return response;
	}

}
