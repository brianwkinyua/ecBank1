package com.EclecticsInterview.bank1.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class MyOTP {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int userAccountId;
	private LocalDateTime created;
	private String otp;
	private LocalDateTime expiry;
	private int loginAttempt;
	
}
