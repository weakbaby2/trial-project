package com.example.demo.project.dto;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "DTO Request Bank Account")
public class BankAccountRequest {

	@Schema(description = "ID unik bank account")
	private Long bankAccountId;
	
	@NotNull(message = "{label.company} {cannot.blank}")
	@Schema(description = "(FK UAM.COMPANY)")
	private Long companyId;
		
	@NotNull(message = "{label.bank} {cannot.blank}")
	@Schema(description = "(FK UAM.bank)")
	private Long bankId;
	
	@NotEmpty(message = "{label.bankAccountNumber} {cannot.blank}")
	@Length(max =64 , message = "{label.bankAccountNumber} {must.length}")
	@Schema(description = "Nomor Akun Bank")
	private String bankAccountNumber;
	
	@NotEmpty(message = "{label.bankAccountName} {cannot.blank}")
	@Length(max = 128, message = "{label.bankAccountName} {must.length}")
	@Schema(description = "Nama Akun Bank")
	private String bankAccountName;
	
	@NotNull(message = "{label.currency} {cannot.blank}")
	@Schema(description = "(FK UAM.postalCountyId)")
	private Long currency;
	
	@Schema(description = "(Nomor Chart of Account)")
	private Long chartOfAccountId;
	
	private String currentUser;
	
}
