package com.example.demo.project.custom.entities;


import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Immutable
@Getter
@Setter
@Subselect("select "
		+ "bt.bank_transaction_beginning_balance_id,"
		+ "bt.bank_transaction_beginning_balance_date,"
		+ "bt.bank_transaction_beginning_balance_amount,"
		+ "bt.bank_account_id "
		+ "from bank_transaction_beginning_balance bt "
		+ "join bank_account b on b.bank_account_id=bt.bank_account_id ")
public class BankTransactionBeginningBalancePartialList {

	@Id
	@Column(name="bank_transaction_beginning_balance_id")
	private Long bankTransactionBeginningBalanceId;
	
	@JsonIgnore
	@Column(name="bank_account_id")
	private Long bankAccountId;
	
	@Transient
	private String bankAccount;
	
	@Schema(type = "string", format = "date")
	@JsonFormat(pattern = "dd MMM yyyy")
	@Column(name = "bank_transaction_beginning_balance_date")
	private Date bankTransactionBeginningBalanceDate;
		
	@Column(name="bank_transaction_beginning_balance_amount")
	private BigDecimal bankTransactionBeginningBalanceAmount;
		
}
