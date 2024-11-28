package com.EclecticsInterview.bank1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class Deposit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	/*
	@ManyToOne(fetch = FetchType.LAZY)
	@ToString.Exclude
	
//	@JoinColumn(name = "fk_userAccount_id")
	@JoinColumn
	private UserAccount userAccount;
	*/
	private int userAccountId;
	
	private Double amount;

	private String description;
	
//	@OneToOne(cascade=CascadeType.ALL)
	private int loanReceipts;
	
//	@OneToOne(cascade=CascadeType.ALL)
	private int investmentSelling;
	
}



