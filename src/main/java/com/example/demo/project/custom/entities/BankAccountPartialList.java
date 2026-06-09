package com.example.demo.project.custom.entities;

import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Immutable
@Getter
@Setter
@Subselect("select "
		+ "b.bank_account_id,"
		+ "b.bank_account_number, "
		+ "b.bank_account_name, "
		+ "from bank_account b ")
public class BankAccountPartialList {

	@Id
	@Column(name="bank_account_id")
	private Long bankAccountId;
	
	@Column(name="bank_account_number")
	private String bankAccountNumber;
	
	@Column(name="bank_account_name")
	private String bankAccountName;
	
	
}
