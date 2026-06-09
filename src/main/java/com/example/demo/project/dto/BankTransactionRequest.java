package com.example.demo.project.dto;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "DTO Request Bank Account Transaction")
public class BankTransactionRequest {

	@Schema(description = "ID unik bank transaction")
	private Long bankTransactionId;
	
	@NotNull(message = "{label.company} {cannot.blank}")
	@Schema(description = "(FK UAM.COMPANY)")
	private Long companyId;
	
	@NotNull(message = "{label.office} {cannot.blank}")
	@Schema(description = "(FK UAM.COMPANY)")
	private Long officeId;
	
	@NotNull(message = "{label.bankAccount} {cannot.blank}")
	@Schema(description = "(FK Bank_Account)")
	private Long bankAccountId;
	
	@NotNull(message = "{label.bankAccountType} {cannot.blank}")
	@Schema(description = "(FK FAM_LOOKUP)")
	private Long bankAccountTypeId;
	
	@NotNull(message = "{label.bankTransactionDate} {cannot.blank}")
	@Schema(type = "string", format = "date", description = "Tanggal Bank Transaction Date")
	@JsonFormat(pattern = "yyyyMMdd")
	private Date bankTransactionDate;
	
	@NotNull(message = "{label.bankAccountOpposite} {cannot.blank}")
	@Schema(description = "(FK Bank_Account Opposite)")
	private Long bankAccountOpposite;
	
	@NotNull(message = "{label.amount} {cannot.blank}")
	@Schema(description = "Nominal Amount")
	private BigDecimal amount;
	
//	@NotNull(message = "{label.status} {cannot.blank}")
	@Schema(description = "(FK FAM_LOOKUP)")
	private Long bankTransactionFlowStatusId;
	
	@Length(max = 255, message = "{label.note} {must.length}")
	@Schema(description = "note")
	private String note;
	
	private String currentUser;
	
}
