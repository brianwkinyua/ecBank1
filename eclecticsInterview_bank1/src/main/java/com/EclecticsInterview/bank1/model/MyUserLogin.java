package com.EclecticsInterview.bank1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class MyUserLogin {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String time;
	private int username;
	private int password;
	private String status;
	private int userAccountId;
	
}
