package com.example.demo.project.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BankTransactionResponse {

	
	private Map<String, Object> companyList;
	private List<Map<String, Object>> officeList;
	private Long bankTransactionId;
	private String bankName;
	private Long bankAccountId;
	private Long bankAccountTypeId;
	private String bankAccountNumber;
	private Long currency;
	@Schema(type = "string", format = "date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date bankTransactionDate;	
	private Map<String, Object> bankAccountOpposite;
	private BigDecimal amount;
	private String note;
	private Long bankTransactionFlowStatusId;
	private String createBy;
	@Schema(type = "string", format = "date")
	@JsonFormat(pattern = "dd MMM yyyy HH:mm:ss")
	private Date createOn;
	private String changeBy;
	@Schema(type = "string", format = "date")
	@JsonFormat(pattern = "dd MMM yyyy HH:mm:ss")
	private Date changeOn;
	
}
