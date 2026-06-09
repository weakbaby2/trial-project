package com.example.demo.project.service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import com.example.demo.project.custom.entities.BankAccountPartialList;
import com.example.demo.project.custom.entities.BankTransaction;
import com.example.demo.project.custom.repositories.BankAccountPartialListRepository;
import com.example.demo.project.custom.repositories.BankAccountPopUpListRepository;
import com.example.demo.project.custom.repository.BankAccountRepository;
import com.example.demo.project.custom.repository.BankTransactionRepository;
import com.example.demo.project.dto.BankAccountRequest;
import com.example.demo.project.dto.BankAccountResponse;
import com.example.demo.project.exception.AlreadyExistsException;
import com.example.demo.project.exception.ResourceNotFoundException;
import com.example.demo.project.utils.PartialList;
import com.example.demo.project.utils.PartialListUtil;

import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService {
	
	
	@Autowired
	BankAccountRepository bankAccountRepository;
	
	@Autowired 
	BankAccountPopUpListRepository bankAccountPopUpListRepository;
	
	@Autowired 
	BankAccountPartialListRepository bankAccountPartialListRepository;
	
	@Autowired
	BankTransactionRepository bankTransactionRepository;
	
	@Autowired
	BankAccountPartialList bankAccountPartialList;
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired 
	PartialListUtil partialListUtil;
	
	@Override
	public Map<String, Object> bankAccountDropDown(Long bankAccountId) throws Exception {
		BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(()->new ResourceNotFoundException("bankAccountId "+bankAccountId+" does not exist"));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("bankAccountId", bankAccount.getBankAccountId());
		result.put("bankAccountNumber", bankAccount.getBankAccountNumber());
		result.put("bankAccountName", bankAccount.getBankAccountName());
		return result;
	}
	
	@Override
	public BankAccount save(BankAccountRequest request) throws Exception {
		BankAccount bankAccount = BankAccount.builder().build();
		
		if(Objects.nonNull(request.getBankAccountId()) && request.getBankAccountId()>0L) {
			Specification<BankAccount> spec = (root, query, builder) -> builder.and(
				    builder.notEqual(root.get("bankAccountId"), request.getBankAccountId()),
				    builder.equal(root.get("bankAccountNumber"), request.getBankAccountNumber())
				);
			List<BankAccount> bankAccountExist = bankAccountRepository.findAll(spec);
			String bankName="Bank ";
			if (bankAccountExist.size()>0) {
				Object[] param = new Object[] {bankName,request.getBankAccountNumber()};
				String message = messageSource.getMessage("already.exist.bankAccount", param, LocaleContextHolder.getLocale());
				throw new AlreadyExistsException(message);
			}
			
			BeanUtils.copyProperties(request,bankAccount);
			bankAccount.setChangeBy(request.getCurrentUser());
			bankAccountRepository.saveAndFlush(bankAccount);
		}else {			
			
			Specification<BankAccount> spec = (root, query, builder) -> builder.and(
				    builder.notEqual(root.get("bankAccountId"), request.getBankAccountId()),
				    builder.equal(root.get("bankAccountNumber"), request.getBankAccountNumber())
				);
			List<BankAccount> bankAccountExist = bankAccountRepository.findAll(spec);
			String bankName="Bank ";

			if (bankAccountExist.size()>0) {
				Object[] param = new Object[] {bankName,request.getBankAccountNumber()};
				String message = messageSource.getMessage("already.exist.bankAccount", param, LocaleContextHolder.getLocale());
				throw new AlreadyExistsException(message);
			}
			
			request.setBankAccountId(0L);
			BeanUtils.copyProperties(request,bankAccount);
			bankAccount.setCreateBy(request.getCurrentUser());
			bankAccountRepository.save(bankAccount);
		}
		return bankAccount;
	}
	
	@Override
	public BankAccountResponse getOne(Long bankAccountId) throws Exception {
		BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(()->new ResourceNotFoundException("bankAccountId "+bankAccountId+" does not exist"));
		BankAccountResponse response = BankAccountResponse.builder().build();
		BeanUtils.copyProperties(bankAccount,response);
		return response;
	}

	@Override
	public void removeOne(Long bankAccountId) throws Exception {
		BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(()->new ResourceNotFoundException("BankAccountId "+bankAccountId+" does not exist"));
		Specification<BankTransaction> spec = (root, query, builder) -> builder.and(
			    builder.equal(root.get("bankAccount.bankAccountId"), bankAccountId)
			);
		List<BankTransaction> bankAccountExist = bankTransactionRepository.findAll(spec);
		
		if (!bankAccountExist.isEmpty()) {
			//Object[] param = new Object[] {messageSource.getMessage("label.bankAccount", null, LocaleContextHolder.getLocale()),messageSource.getMessage("label.bankTransaction", null, LocaleContextHolder.getLocale())};
    		String message = messageSource.getMessage("not.delete", null, LocaleContextHolder.getLocale());
    		throw new ResourceNotFoundException(message);
        }
		bankAccountRepository.delete(bankAccount);
	}

	@Override
	public PartialList<BankAccountPartialList> partialList(int pageNo, int pageSize, String sortField,
			String sortDirection, Long companyId, Long bankId, String bankAccountNumber,
			String bankAccountName) throws Exception {
		Specification<BankAccountPartialList> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (Objects.nonNull(bankAccountNumber)) {
            	predicates.add(builder.like(builder.lower(root.get("bankAccountNumber")), "%" + bankAccountNumber.toLowerCase() + "%"));
            }
            if (Objects.nonNull(bankAccountName)) {
            	predicates.add(builder.like(builder.lower(root.get("bankAccountName")), "%" + bankAccountName.toLowerCase() + "%"));
            }

            if (StringUtils.hasText(sortField)&&!sortField.equals("bankAccountNumber")&&!sortField.equals("bankAccountName")) {
            	if (sortDirection.equals("asc")) {
            		query.orderBy(builder.asc(root.get(sortField)));
                } else if (sortDirection.equals("desc")) {
                	query.orderBy(builder.desc(root.get(sortField)));
                }
    		} else {
    			query.orderBy(builder.desc(root.get("bankAccountId")));
    		}
            
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
        
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<BankAccountPartialList> bankAccountList = bankAccountPartialListRepository.findAll(specification, pageable);
		if(!bankAccountList.getContent().isEmpty()) {
			if (StringUtils.hasText(sortField)&&sortField.equals("bankAccountName")) {
				if (sortDirection.equals("asc")) {
					List<BankAccountPartialList> sortedList = new ArrayList<>(bankAccountList.getContent());
				    Collections.sort(sortedList, Comparator.comparing(BankAccountPartialList::getBankAccountName));
				    bankAccountList = new PageImpl<>(sortedList, pageable, sortedList.size());			
				} else if (sortDirection.equals("desc")) {
					List<BankAccountPartialList> sortedList = new ArrayList<>(bankAccountList.getContent());
				    Collections.sort(sortedList, Comparator.comparing(BankAccountPartialList::getBankAccountName).reversed());
				    bankAccountList = new PageImpl<>(sortedList, pageable, sortedList.size());				
				}
			}
			
		}
		return partialListUtil.createPartialList(bankAccountList);
	}
	
}
