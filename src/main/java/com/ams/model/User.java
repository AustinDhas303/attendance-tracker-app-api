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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name="first_name",length=30) 
	private String firstName;
	
	@Column(name="last_name",length=30) 
	private String lastName;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "email_id",length=45)
	private String emailId;
	
	@Column(name="status")
	private int status;
	
	@Column(name = "contact_no",length=10)
	private String contactNo;
	
	@Column(name = "address",length=45)
	private String address;
	
	@Column(name = "created_at",updatable =false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_At;
	
	@Column(name = "updated_at")
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_At;
	
	@ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
