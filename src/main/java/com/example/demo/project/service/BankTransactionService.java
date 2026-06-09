package com.example.demo.project.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.example.demo.project.custom.entities.BankTransaction;
import com.example.demo.project.custom.entities.BankTransactionPartialList;
import com.example.demo.project.dto.BankTransactionRequest;
import com.example.demo.project.dto.BankTransactionResponse;
import com.example.demo.project.utils.PartialList;

public interface BankTransactionService {
	
	BankTransaction save(BankTransactionRequest request) throws Exception;
	BankTransactionResponse getOne(Long bankTransactionId) throws Exception;
	void removeOne(Long bankTransactionId) throws Exception;
	PartialList<BankTransactionPartialList> partialList(int pageNo, int pageSize, String sortField, String sortDirection, Long bankAccountId, Date bankTransactionStartDate,Date bankTransactionEndDate, String notes, BigDecimal amountStart, BigDecimal amountEnd) throws Exception;
	Map<String, Object> bankAccountPopUp(Long bankAccountId) throws Exception;

}
