package com.example.demo.project.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.project.custom.entities.BankAccountPopUpList;
import com.example.demo.project.custom.entities.BankTransactionPartialList;
import com.example.demo.project.dto.BankTransactionRequest;
import com.example.demo.project.dto.BankTransactionResponse;
import com.example.demo.project.service.BankAccountService;
import com.example.demo.project.service.BankTransactionService;
import com.example.demo.project.utils.JSONResponse;
import com.example.demo.project.utils.PagingRequest;
import com.example.demo.project.utils.PartialList;
import com.example.demo.project.utils.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/bankTransaction")
public class BankTransactionController {

	@Autowired BankTransactionService bankTransactionService;
	
	@Autowired BankAccountService bankAccountService;
	
	
	@Operation(summary = "Create New Bank Transaction")
	@PostMapping("/save")
	public ResponseEntity<JSONResponse<Long>> save(@RequestBody BankTransactionRequest request) throws Exception {
		bankTransactionService.save(request);
		return new ResponseEntity<JSONResponse<Long>>(JSONResponse.<Long>builder().rc(ResponseCode.OK).data(request.getBankTransactionId()).build(), HttpStatus.OK);
	}
	
	@Operation(summary = "Get One Bank Transaction")
	@GetMapping("/{bankTransactionId}")
	public ResponseEntity<JSONResponse<BankTransactionResponse>> getOne(@PathVariable("bankTransactionId") Long bankTransactionId) throws Exception {
		BankTransactionResponse bankTransactionResponse = bankTransactionService.getOne(bankTransactionId);
		return new ResponseEntity<JSONResponse<BankTransactionResponse>>(JSONResponse.<BankTransactionResponse>builder().rc(ResponseCode.OK).data(bankTransactionResponse).build(), HttpStatus.OK);
	}
	
	@Operation(summary = "Delete Bank Transaction By ID Primary")
    @DeleteMapping("/delete/{bankTransactionId}")
    public ResponseEntity<JSONResponse<Long>> delete(@PathVariable("bankTransactionId") Long bankTransactionId) throws Exception {
		bankTransactionService.removeOne(bankTransactionId);
		return new ResponseEntity<JSONResponse<Long>>(JSONResponse.<Long>builder().rc(ResponseCode.OK).build(), HttpStatus.OK);
    }
	
	@Operation(summary = "Get Bank Transaction By Paging Request, attribute filter : companyId,officeId,bankId,bankTransactionStartDate,bankTransactionEndDate,bankTransactionFlowStatusId")
    @PostMapping("/partialList")
    public ResponseEntity<JSONResponse<PartialList<BankTransactionPartialList>>> getPopup(@RequestBody PagingRequest pagingRequest, HttpServletRequest req) throws Exception {
    	
		Map<String, Object> filterParam = pagingRequest.getFilterParam();
    	
    	String bankAccountIdFilter = filterParam.get("bankAccountId") == null ? null : String.valueOf(filterParam.get("bankAccountId"));
    	Long bankAccountId = null;
    	if(bankAccountIdFilter!=null) bankAccountId = Long.parseLong(bankAccountIdFilter);
   
    	String bankAccountTypeIdFilter = filterParam.get("bankAccountType") == null ? null : String.valueOf(filterParam.get("bankAccountType"));
    	Long bankAccountTypeId = null;
    	if(bankAccountTypeIdFilter!=null) bankAccountTypeId = Long.parseLong(bankAccountTypeIdFilter);
    	
     	String bankAccountOppositeIdFilter = filterParam.get("bankAccountOppositeId") == null ? null : String.valueOf(filterParam.get("bankAccountOppositeId"));
    	Long bankAccountOppositeId = null;
    	if(bankAccountOppositeIdFilter!=null) bankAccountOppositeId = Long.parseLong(bankAccountOppositeIdFilter);
    	
    	String amountStartFilter = filterParam.get("amountStart") == null ? null : String.valueOf(filterParam.get("amountStart"));
    	BigDecimal amountStart = null;
    	if(amountStartFilter!=null) amountStart = new BigDecimal(amountStartFilter);
    	
    	String amountEndFilter = filterParam.get("amountEnd") == null ? null : String.valueOf(filterParam.get("amountEnd"));
    	BigDecimal amountEnd = null;
    	if(amountEndFilter!=null) amountEnd = new BigDecimal(amountEndFilter);
    	
		Date bankTransactionStartDate = pagingRequest.getDateValue("bankTransactionStartDate", "yyyy-MM-dd");
		Date bankTransactionEndDate = pagingRequest.getDateValue("bankTransactionEndDate", "yyyy-MM-dd");
		
		String notes = (String) filterParam.get("notes");
		
		PartialList<BankTransactionPartialList> res = bankTransactionService.partialList(pagingRequest.getPageNo(), pagingRequest.getPageSize(), pagingRequest.getOrderBy(), pagingRequest.getDirective(), bankAccountId, bankTransactionStartDate, bankTransactionEndDate, notes, amountStart, amountEnd);
		return new ResponseEntity<JSONResponse<PartialList<BankTransactionPartialList>>>(JSONResponse.<PartialList<BankTransactionPartialList>>builder().rc(ResponseCode.OK).data(res).build(), HttpStatus.OK);
		
    }
	
}
