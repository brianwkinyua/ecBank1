package com.EclecticsInterview.bank1.model;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class UserAccountApplication {
	
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
	private int personId;
	
	private String date;
	private String targetUsername;
	private String status;
	private String updated;
	
//	private UserAccount userAccount;
	private int userAccountId;
	
//	@OneToOne(cascade=CascadeType.ALL)
//	private UserAccount account;
	

	
	
}
