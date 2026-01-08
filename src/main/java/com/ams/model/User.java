package com.ams.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.ams.model.Role;
import com.ams.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "UserId")
	private Long userId;
	
	@Column(name="FirstName",length=30) 
	private String firstName;
	
	@Column(name="LastName",length=30) 
	private String lastName;
	
	@Column(name = "Password")
	private String password;
	
	@Column(name = "Email",length=45)
	private String emailId;
	
	@Column(name="Status")
	private int status;
	
	@Column(name = "ContactNo",length=10)
	private String contactNo;
	
	@Column(name = "Address",length=45)
	private String address;
	
	@Column(name = "CreatedAt",updatable =false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_At;
	
	@Column(name = "UpdatedAt")
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_At;
	
	@ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
