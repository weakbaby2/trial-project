package com.example.demo.project.controller;

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

import com.example.demo.project.custom.entities.BankAccountPartialList;
import com.example.demo.project.dto.BankAccountRequest;
import com.example.demo.project.dto.BankAccountResponse;
import com.example.demo.project.service.BankAccountService;
import com.example.demo.project.utils.JSONResponse;
import com.example.demo.project.utils.PagingRequest;
import com.example.demo.project.utils.PartialList;
import com.example.demo.project.utils.ResponseCode;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/bankAccount")
public class BankAccountController {

	@Autowired BankAccountService bankAccountService;
	
	@Operation(summary = "Create New Bank Account")
	@PostMapping("/save")
	public ResponseEntity<JSONResponse<Long>> save(@RequestBody BankAccountRequest request) throws Exception {
		bankAccountService.save(request);
		return new ResponseEntity<JSONResponse<Long>>(JSONResponse.<Long>builder().rc(ResponseCode.OK).data(request.getBankAccountId()).build(), HttpStatus.OK);
	}
	
	@Operation(summary = "Get One Bank Account")
	@GetMapping("/{bankAccountId}")
	public ResponseEntity<JSONResponse<BankAccountResponse>> getOne(@PathVariable("bankAccountId") Long bankAccountId) throws Exception {
		BankAccountResponse bankAccountResponse = bankAccountService.getOne(bankAccountId);
		return new ResponseEntity<JSONResponse<BankAccountResponse>>(JSONResponse.<BankAccountResponse>builder().rc(ResponseCode.OK).data(bankAccountResponse).build(), HttpStatus.OK);
	}
	
	@Operation(summary = "Delete Bank Account By ID Primary")
    @DeleteMapping("/delete/{bankAccountId}")
    public ResponseEntity<JSONResponse<Long>> delete(@PathVariable("bankAccountId") Long bankAccountId) throws Exception {
		bankAccountService.removeOne(bankAccountId);
		return new ResponseEntity<JSONResponse<Long>>(JSONResponse.<Long>builder().rc(ResponseCode.OK).build(), HttpStatus.OK);
    }
	
	@Operation(summary = "Get Bank Account By Paging Request, attribute filter : companyId,bankId,bankAccountNumber,bankAccountName")
    @PostMapping("/partialList")
    public ResponseEntity<JSONResponse<PartialList<BankAccountPartialList>>> getPartialList(@RequestBody PagingRequest pagingRequest, HttpServletRequest req) throws Exception {
    	
		Map<String, Object> filterParam = pagingRequest.getFilterParam();
    	
		Long companyUserId = Long.parseLong(req.getHeader("findTask2") != null ?req.getHeader("findTask2") :"0" );
		
		String companyIdFilter = filterParam.get("companyId") == null ? null : String.valueOf(filterParam.get("companyId"));
    	Long companyId = null;
    	if(companyIdFilter!=null) companyId = Long.parseLong(companyIdFilter);
    	
    	String bankIdFilter = filterParam.get("bankId") == null ? null : String.valueOf(filterParam.get("bankId"));
    	Long bankId = null;
    	if(bankIdFilter!=null) bankId = Long.parseLong(bankIdFilter);
    	
		String bankAccountName = (String) filterParam.get("bankAccountName");
		
		String bankAccountNumber = (String) filterParam.get("bankAccountNumber");

		PartialList<BankAccountPartialList> res = bankAccountService.partialList(pagingRequest.getPageNo(), pagingRequest.getPageSize(), pagingRequest.getOrderBy(), pagingRequest.getDirective(), companyId, bankId,bankAccountNumber, bankAccountName);
		return new ResponseEntity<JSONResponse<PartialList<BankAccountPartialList>>>(JSONResponse.<PartialList<BankAccountPartialList>>builder().rc(ResponseCode.OK).data(res).build(), HttpStatus.OK);
    }
	
}
