package com.example.demo.project.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.demo.project.custom.entities.BankAccount;
import com.example.demo.project.custom.entities.BankTransaction;
import com.example.demo.project.custom.entities.BankTransactionPartialList;
import com.example.demo.project.custom.repositories.BankTransactionPartialListRepository;
import com.example.demo.project.custom.repository.BankAccountRepository;
import com.example.demo.project.custom.repository.BankTransactionRepository;
import com.example.demo.project.dto.BankTransactionRequest;
import com.example.demo.project.dto.BankTransactionResponse;
import com.example.demo.project.exception.AlreadyExistsException;
import com.example.demo.project.exception.ResourceNotFoundException;
import com.example.demo.project.utils.PartialList;
import com.example.demo.project.utils.PartialListUtil;

import jakarta.persistence.criteria.Predicate;



@Service
@Transactional
public class BankTransactionServiceImpl implements BankTransactionService {

	@Autowired BankTransactionPartialListRepository bankTransactionPartialListRepository;
	@Autowired BankAccountRepository bankAccountRepository;
	@Autowired BankTransactionRepository bankTransactionRepository;
	@Autowired PartialListUtil partialListUtil;

	@Autowired MessageSource messageSource;
	
	@Override
	public BankTransaction save(BankTransactionRequest request) throws Exception {
		BankTransaction bankTransaction = BankTransaction.builder().build();
		
		BankAccount bankAccount = null;
		if(Objects.nonNull(request.getBankAccountId()) && request.getBankAccountId()>0L) {
			bankAccount = bankAccountRepository.findById(request.getBankAccountId()).orElseThrow(()->new BadRequestException("bankAccountId " +request.getBankAccountId()+" "+messageSource.getMessage("does.not.exist", null, LocaleContextHolder.getLocale())));
		}
		
		if(Objects.nonNull(request.getBankTransactionId()) && request.getBankTransactionId()>0L) {
			if(request.getAmount().equals(BigDecimal.ZERO)) {
				String message = messageSource.getMessage("amount.zero", null, LocaleContextHolder.getLocale());
				throw new ResourceNotFoundException(message);
			}
			
			BeanUtils.copyProperties(request,bankTransaction);
			bankTransaction.setBankAccount(bankAccount);
			bankTransaction.setChangeBy(request.getCurrentUser());
			bankTransactionRepository.saveAndFlush(bankTransaction);
		}else {
			if(request.getAmount().equals(BigDecimal.ZERO)) {
				String message = messageSource.getMessage("amount.zero", null, LocaleContextHolder.getLocale());
				throw new ResourceNotFoundException(message);
			}
			
			request.setBankTransactionId(0L);
			BeanUtils.copyProperties(request,bankTransaction);
			bankTransaction.setBankAccount(bankAccount);
			bankTransaction.setCreateBy(request.getCurrentUser());
			bankTransactionRepository.save(bankTransaction);
		}
		return bankTransaction;
	}
	
	@Override
	public BankTransactionResponse getOne(Long bankTransactionId) throws Exception {
		BankTransaction bankTransaction = bankTransactionRepository.findById(bankTransactionId).orElseThrow(()->new ResourceNotFoundException("bankTransactionId "+bankTransactionId+" does not exist"));
		BankTransactionResponse response = BankTransactionResponse.builder().build();
		
		BeanUtils.copyProperties(bankTransaction,response);
		if(bankTransaction.getBankAccount()!=null) {
			BankAccount bankAccount = bankAccountRepository.findById(bankTransaction.getBankAccount().getBankAccountId()).orElseThrow(()->new BadRequestException("bankAccountId"+bankTransaction.getBankAccount().getBankAccountId()+" "+messageSource.getMessage("does.not.exist", null, LocaleContextHolder.getLocale())));
			response.setBankAccountId(bankAccount.getBankAccountId());
			response.setBankAccountNumber(bankAccount.getBankAccountNumber());
		}
		
		response.setAmount(bankTransaction.getAmount().abs());
		return response;
	}

	@Override
	public void removeOne(Long bankTransactionId) throws Exception {
		BankTransaction bankTransaction = bankTransactionRepository.findById(bankTransactionId).orElseThrow(()->new ResourceNotFoundException("bankTransactionId "+bankTransactionId+" does not exist"));
		bankTransactionRepository.delete(bankTransaction);
	}

	@Override
	public PartialList<BankTransactionPartialList> partialList(int pageNo, int pageSize, String sortField, 
			String sortDirection, Long bankAccountId, Date bankTransactionStartDate,Date bankTransactionEndDate, 
			String notes, BigDecimal amountStart, BigDecimal amountEnd) throws Exception {
		
        if (Objects.nonNull(bankTransactionEndDate) && Objects.nonNull(bankTransactionStartDate)) {
			if(bankTransactionStartDate.after(bankTransactionEndDate)){
				String message = messageSource.getMessage("startBankTransactionDate.after", null, LocaleContextHolder.getLocale());
				throw new BadRequestException(message);
			}
        }
        
        if (Objects.nonNull(amountStart) && amountStart.compareTo(BigDecimal.ZERO)>0) {
			if(Objects.isNull(amountEnd)) {
				Object[] param = new Object[] {messageSource.getMessage("label.amountEnd", null, LocaleContextHolder.getLocale())};
				String message = messageSource.getMessage("not.blank", param, LocaleContextHolder.getLocale());
				throw new BadRequestException(message);
			}
		}
		
        if (Objects.nonNull(amountEnd) && amountEnd.compareTo(BigDecimal.ZERO)>0) {
			if(Objects.isNull(amountStart)) {
				Object[] param = new Object[] {messageSource.getMessage("label.amountStart", null, LocaleContextHolder.getLocale())};
				String message = messageSource.getMessage("not.blank", param, LocaleContextHolder.getLocale());
				throw new BadRequestException(message);
			}
		}
		
		Specification<BankTransactionPartialList> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (Objects.nonNull(bankAccountId) && bankAccountId>0L) {
            	predicates.add(builder.equal(root.get("bankAccountId"), bankAccountId));
            }
            if (Objects.nonNull(bankTransactionStartDate)) {
            	predicates.add(builder.greaterThanOrEqualTo(root.get("bankTransactionDate"), bankTransactionStartDate));
            }
            if (Objects.nonNull(bankTransactionEndDate)) {
            	predicates.add(builder.lessThanOrEqualTo(root.get("bankTransactionDate"), bankTransactionEndDate));
            }
            
            if (Objects.nonNull(amountStart)) {
            	predicates.add(builder.greaterThanOrEqualTo(root.get("amount"), amountStart));
            }
            if (Objects.nonNull(amountEnd)) {
            	predicates.add(builder.lessThanOrEqualTo(root.get("amount"), amountEnd));
            }
            
            if (Objects.nonNull(notes)) {
            	predicates.add(builder.like(builder.lower(root.get("note")), "%" + notes.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(sortField)&&!sortField.equals("companyName")&&!sortField.equals("officeName")&&!sortField.equals("bankAccount")&&!sortField.equals("bankTransactionFlowStatusName")&&!sortField.equals("bankAccountTypeName")) {
            	if (sortDirection.equals("asc")) {
            		query.orderBy(builder.asc(root.get(sortField)));
                } else if (sortDirection.equals("desc")) {
                	query.orderBy(builder.desc(root.get(sortField)));
                }
    		} else {
    			query.orderBy(builder.desc(root.get("bankTransactionId")));
    		}
            
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
        
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<BankTransactionPartialList> bankTransactionList = bankTransactionPartialListRepository.findAll(specification, pageable);
		if(!bankTransactionList.getContent().isEmpty()) {
			for(BankTransactionPartialList s : bankTransactionList) {
				BankAccount bankAccount = bankAccountRepository.findById(s.getBankAccountId()).orElseThrow(()->new BadRequestException("bankAccountId"+s.getBankAccountId()+" "+messageSource.getMessage("does.not.exist", null, LocaleContextHolder.getLocale())));
				s.setBankAccount(bankAccount.getBankAccountNumber());
			}
			
			if (StringUtils.hasText(sortField)&&sortField.equals("bankAccount")) {
				if (sortDirection.equals("asc")) {
					List<BankTransactionPartialList> sortedList = new ArrayList<>(bankTransactionList.getContent());
				    Collections.sort(sortedList, Comparator.comparing(BankTransactionPartialList::getBankAccount));
				    bankTransactionList = new PageImpl<>(sortedList, pageable, sortedList.size());			
				} else if (sortDirection.equals("desc")) {
					List<BankTransactionPartialList> sortedList = new ArrayList<>(bankTransactionList.getContent());
				    Collections.sort(sortedList, Comparator.comparing(BankTransactionPartialList::getBankAccount).reversed());
				    bankTransactionList = new PageImpl<>(sortedList, pageable, sortedList.size());
				}
			}
		}
		return partialListUtil.createPartialList(bankTransactionList);
	}

	@Override
	public Map<String, Object> bankAccountPopUp(Long bankAccountId) throws Exception {
		BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(()->new ResourceNotFoundException("bankAccountId "+bankAccountId+" does not exist"));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("bankAccountId", bankAccount.getBankAccountId());
		result.put("bankAccountNumber", bankAccount.getBankAccountNumber());
		result.put("bankAccountName", bankAccount.getBankAccountName());
		return result;
	}

}
