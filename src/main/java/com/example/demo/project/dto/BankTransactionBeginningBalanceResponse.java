package com.example.demo.project.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BankTransactionBeginningBalanceResponse {

	
	private Long bankTransactionBeginningBalanceId;
	private String bankName;
	private Long bankAccountId;
	private String bankAccountNumber;
	@Schema(type = "string", format = "date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date bankTransactionBeginningBalanceDate;	
	private BigDecimal bankTransactionBeginningBalanceAmount;
	private String createBy;
	@Schema(type = "string", format = "date")
	@JsonFormat(pattern = "dd MMM yyyy HH:mm:ss")
	private Date createOn;
	private String changeBy;
	@Schema(type = "string", format = "date")
	@JsonFormat(pattern = "dd MMM yyyy HH:mm:ss")
	private Date changeOn;
	
}
