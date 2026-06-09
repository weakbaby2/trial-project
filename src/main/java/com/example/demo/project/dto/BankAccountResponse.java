package com.example.demo.project.dto;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BankAccountResponse {

	private Map<String, Object> companyList;
	private Long bankAccountId;
	private String bankAccountName;
	private String bankAccountNumber;
	private Long bankId;
	private CountryResponse currency;
	private String chartOfAccount;
	private Long chartOfAccountId;
	private String chartOfAccountNumber;
	private Long chartOfAccountTypeId;
	private String createBy;
	@Schema(type = "string", format = "date")
	@JsonFormat(pattern = "dd MMM yyyy HH:mm:ss")
	private Date createOn;
	private String changeBy;
	
	@Schema(type = "string", format = "date")
	@JsonFormat(pattern = "dd MMM yyyy HH:mm:ss")
	private Date changeOn;
	
	
	@Getter
	@Setter
	public static class CountryResponse {
		Long postalCountryId;
		String postalCountryCurrency;
	}
	
}
