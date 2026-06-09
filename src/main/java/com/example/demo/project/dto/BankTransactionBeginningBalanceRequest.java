package com.example.demo.project.dto;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "DTO Request Bank Transaction Beginning Balance")
public class BankTransactionBeginningBalanceRequest {

	@Schema(description = "ID unik Bank Transaction Beginning Balance")
	private Long bankTransactionBeginningBalanceId;
	
	@NotNull(message = "{label.bankAccount} {cannot.blank}")
	@Schema(description = "(FK Bank_Account)")
	private Long bankAccountId;
	
	@NotNull(message = "{label.bankTransactionBeginningBalanceDate} {cannot.blank}")
	@Schema(type = "string", format = "date", description = "Tanggal Bank Transaction Beginning Balance Date")
	@JsonFormat(pattern = "yyyyMMdd")
	private Date bankTransactionBeginningBalanceDate;
	
	@NotNull(message = "{label.bankTransactionBeginningBalanceAmount} {cannot.blank}")
	@Schema(description = "Nominal Bank Transaction Beginning Balance Amount")
	private BigDecimal bankTransactionBeginningBalanceAmount;
		
	private String currentUser;
	
}
