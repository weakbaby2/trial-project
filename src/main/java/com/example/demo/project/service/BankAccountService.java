package com.example.demo.project.service;

import java.util.Map;

import com.example.demo.project.custom.entities.BankAccount;
import com.example.demo.project.custom.entities.BankAccountPartialList;
import com.example.demo.project.dto.BankAccountRequest;
import com.example.demo.project.dto.BankAccountResponse;
import com.example.demo.project.utils.PartialList;

public interface BankAccountService {
	
	Map<String, Object> bankAccountDropDown(Long bankAccountId) throws Exception;
	
	BankAccount save(BankAccountRequest request) throws Exception;
	BankAccountResponse getOne(Long bankAccountId) throws Exception;
	void removeOne(Long bankAccountId) throws Exception;
	
	PartialList<BankAccountPartialList> partialList(int pageNo, int pageSize, String sortField,
			String sortDirection, Long companyId, Long bankId, String bankAccountNumber,
			String bankAccountName) throws Exception;

}
