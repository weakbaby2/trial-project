package com.example.demo.project.controller;

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
import com.example.demo.project.custom.entities.BankTransactionBeginningBalancePartialList;
import com.example.demo.project.dto.BankTransactionBeginningBalanceRequest;
import com.example.demo.project.dto.BankTransactionBeginningBalanceResponse;
import com.example.demo.project.service.BankAccountService;
import com.example.demo.project.service.BankTransactionBeginningBalanceService;
import com.example.demo.project.utils.JSONResponse;
import com.example.demo.project.utils.PagingRequest;
import com.example.demo.project.utils.PartialList;
import com.example.demo.project.utils.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/bankTransactionBeginningBalance")
public class BankTransactionBeginningBalanceController {

	@Autowired BankTransactionBeginningBalanceService bankTransactionBeginningBalanceService;
	
	@Autowired BankAccountService bankAccountService;
	
	@Operation(summary = "Create New Bank Transaction Beginning Balance")
	@PostMapping("/save")
	public ResponseEntity<JSONResponse<Long>> save(@RequestBody BankTransactionBeginningBalanceRequest request) throws Exception {
		bankTransactionBeginningBalanceService.save(request);
		return new ResponseEntity<JSONResponse<Long>>(JSONResponse.<Long>builder().rc(ResponseCode.OK).data(request.getBankTransactionBeginningBalanceId()).build(), HttpStatus.OK);
	}
	
	@Operation(summary = "Get One Bank Transaction Beginning Balance")
	@GetMapping("/{bankTransactionBeginningBalanceId}")
	public ResponseEntity<JSONResponse<BankTransactionBeginningBalanceResponse>> getOne(@PathVariable("bankTransactionBeginningBalanceId") Long bankTransactionBeginningBalanceId) throws Exception {
		BankTransactionBeginningBalanceResponse BankTransactionBeginningBalanceResponse = bankTransactionBeginningBalanceService.getOne(bankTransactionBeginningBalanceId);
		return new ResponseEntity<JSONResponse<BankTransactionBeginningBalanceResponse>>(JSONResponse.<BankTransactionBeginningBalanceResponse>builder().rc(ResponseCode.OK).data(BankTransactionBeginningBalanceResponse).build(), HttpStatus.OK);
	}
	
	@Operation(summary = "Delete Bank Transaction Beginning Balance By ID Primary")
    @DeleteMapping("/delete/{bankTransactionBeginningBalanceId}")
    public ResponseEntity<JSONResponse<Long>> delete(@PathVariable("bankTransactionBeginningBalanceId") Long bankTransactionBeginningBalanceId) throws Exception {
		bankTransactionBeginningBalanceService.removeOne(bankTransactionBeginningBalanceId);
		return new ResponseEntity<JSONResponse<Long>>(JSONResponse.<Long>builder().rc(ResponseCode.OK).build(), HttpStatus.OK);
    }
	
	@Operation(summary = "Get Bank Transaction Beginning Balance By Paging Request, attribute filter : companyId,officeId,bankAccountId,bankTransactionBeginningBalanceDate")
    @PostMapping("/partialList")
    public ResponseEntity<JSONResponse<PartialList<BankTransactionBeginningBalancePartialList>>> getPopup(@RequestBody PagingRequest pagingRequest, HttpServletRequest req) throws Exception {
    	
		Map<String, Object> filterParam = pagingRequest.getFilterParam();
    	
    	String bankAccountIdFilter = filterParam.get("bankAccountId") == null ? null : String.valueOf(filterParam.get("bankAccountId"));
    	Long bankAccountId = null;
    	if(bankAccountIdFilter!=null) bankAccountId = Long.parseLong(bankAccountIdFilter);
   
		Date bankTransactionBeginningBalanceDate = pagingRequest.getDateValue("bankTransactionBeginningBalanceDate", "yyyy-MM-dd");
		
		PartialList<BankTransactionBeginningBalancePartialList> res = bankTransactionBeginningBalanceService.partialList(pagingRequest.getPageNo(), pagingRequest.getPageSize(), pagingRequest.getOrderBy(), pagingRequest.getDirective(), bankAccountId, bankTransactionBeginningBalanceDate);
		return new ResponseEntity<JSONResponse<PartialList<BankTransactionBeginningBalancePartialList>>>(JSONResponse.<PartialList<BankTransactionBeginningBalancePartialList>>builder().rc(ResponseCode.OK).data(res).build(), HttpStatus.OK);
		
    }
	
	@Operation(summary = "Get Bank Account by Dropdown")
	@GetMapping("/bankTransactionBeginningBalance/{bankAccountId}")
	public ResponseEntity<JSONResponse<Map<String, Object>>> bankAccountPopup(@PathVariable("bankAccountId") Long bankAccountId) throws Exception {
		Map<String, Object> bankAccountResponse = bankAccountService.bankAccountDropDown(bankAccountId);
		return new ResponseEntity<JSONResponse<Map<String, Object>>>(JSONResponse.<Map<String, Object>>builder().rc(ResponseCode.OK).data(bankAccountResponse).build(), HttpStatus.OK); 
	}
	
}
