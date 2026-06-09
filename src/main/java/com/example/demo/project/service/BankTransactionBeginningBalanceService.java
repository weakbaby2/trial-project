package com.example.demo.project.service;

import java.util.Date;

import com.example.demo.project.custom.entities.BankTransactionBeginningBalance;
import com.example.demo.project.custom.entities.BankTransactionBeginningBalancePartialList;
import com.example.demo.project.dto.BankTransactionBeginningBalanceRequest;
import com.example.demo.project.dto.BankTransactionBeginningBalanceResponse;
import com.example.demo.project.utils.PartialList;


public interface BankTransactionBeginningBalanceService {
	
	BankTransactionBeginningBalance save(BankTransactionBeginningBalanceRequest request) throws Exception;
	BankTransactionBeginningBalanceResponse getOne(Long bankTransactionBeginningBalanceId) throws Exception;
	void removeOne(Long bankTransactionBeginningBalanceId) throws Exception;
	PartialList<BankTransactionBeginningBalancePartialList> partialList(int pageNo, int pageSize, String sortField, String sortDirection, Long bankAccountId, Date bankTransactionBeginningBalanceDate) throws Exception;
	
}
