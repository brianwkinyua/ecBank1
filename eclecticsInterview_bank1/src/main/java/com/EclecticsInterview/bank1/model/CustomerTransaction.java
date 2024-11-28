package com.EclecticsInterview.bank1.model;

import java.time.LocalDateTime;

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
public class CustomerTransaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	/*	
//	@OneToOne(cascade=CascadeType.ALL)
	@ManyToOne(fetch = FetchType.LAZY)
	@ToString.Exclude
	
//	@JoinColumn(name = "fk_person_id")
//	@JoinColumn
	@JoinColumn(name = "person_id", referencedColumnName = "id")
	private Person person;
*/
	private int customerId;
	
	private String transactionType;
	private Double amount;
	private String description;
	private LocalDateTime created;
	private LocalDateTime updated;
	private String status;
	

	
	
}
