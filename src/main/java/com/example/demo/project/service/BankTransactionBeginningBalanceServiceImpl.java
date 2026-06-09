package com.example.demo.project.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
import org.springframework.util.StringUtils;

import com.example.demo.project.custom.entities.BankAccount;
import com.example.demo.project.custom.entities.BankTransactionBeginningBalance;
import com.example.demo.project.custom.entities.BankTransactionBeginningBalancePartialList;
import com.example.demo.project.custom.repositories.BankTransactionBeginningBalancePartialListRepository;
import com.example.demo.project.custom.repository.BankAccountRepository;
import com.example.demo.project.custom.repository.BankTransactionBeginningBalanceRepository;
import com.example.demo.project.dto.BankTransactionBeginningBalanceRequest;
import com.example.demo.project.dto.BankTransactionBeginningBalanceResponse;
import com.example.demo.project.exception.AlreadyExistsException;
import com.example.demo.project.exception.ResourceNotFoundException;
import com.example.demo.project.utils.PartialList;
import com.example.demo.project.utils.PartialListUtil;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BankTransactionBeginningBalanceServiceImpl implements BankTransactionBeginningBalanceService {

	@Autowired BankTransactionBeginningBalancePartialListRepository bankTransactionBeginningBalancePartialListRepository;
	@Autowired BankTransactionBeginningBalanceRepository bankTransactionBeginningBalanceRepository;
	@Autowired BankAccountRepository bankAccountRepository;
	@Autowired MessageSource messageSource;
	
	@Autowired PartialListUtil partialListUtil;
	
	@Override
	public BankTransactionBeginningBalance save(BankTransactionBeginningBalanceRequest request) throws Exception {
		BankTransactionBeginningBalance bankTransactionBeginningBalance = BankTransactionBeginningBalance.builder().build();
		
		if(Objects.nonNull(request.getBankTransactionBeginningBalanceId()) && request.getBankTransactionBeginningBalanceId()>0L) {		
			if(Objects.nonNull(request.getBankAccountId()) && request.getBankAccountId()>0L) {
				BankAccount bankAccount = bankAccountRepository.findById(request.getBankAccountId()).orElseThrow(()->new BadRequestException("bankAccountId " +request.getBankAccountId()+" "+messageSource.getMessage("does.not.exist", null, LocaleContextHolder.getLocale())));
				Specification<BankAccount> spec = (root, query, builder) -> builder.and(
					    builder.notEqual(root.get("bankTransactionBeginningBalanceId"), request.getBankAccountId()),
					    builder.equal(root.get("bankAccount"), bankAccount)
					);
				List<BankAccount> bankAccountExist = bankAccountRepository.findAll(spec);

				if (bankAccountExist.size()>0) {
					String message = messageSource.getMessage("bankTransactionBeginningBalance.exists", null, LocaleContextHolder.getLocale());
					throw new AlreadyExistsException(message);
				}
				bankTransactionBeginningBalance.setBankAccount(bankAccount);
			}
			
			BeanUtils.copyProperties(request,bankTransactionBeginningBalance);
			bankTransactionBeginningBalance.setChangeBy(request.getCurrentUser());
			bankTransactionBeginningBalanceRepository.saveAndFlush(bankTransactionBeginningBalance);
		}else {
			if(Objects.nonNull(request.getBankAccountId()) && request.getBankAccountId()>0L) {
				BankAccount bankAccount = bankAccountRepository.findById(request.getBankAccountId()).orElseThrow(()->new BadRequestException("bankAccountId " +request.getBankAccountId()+" "+messageSource.getMessage("does.not.exist", null, LocaleContextHolder.getLocale())));

				Specification<BankTransactionBeginningBalance> transactionSpec = (root, query, criteriaBuilder) -> 
	            criteriaBuilder.equal(root.get("bankAccount"), bankAccount);
	            
				Optional<BankTransactionBeginningBalance> isExist = bankTransactionBeginningBalanceRepository.findOne(transactionSpec);
				if (isExist.isPresent()) {
					String message = messageSource.getMessage("bankTransactionBeginningBalance.exists", null, LocaleContextHolder.getLocale());
					throw new AlreadyExistsException(message);
				}
				bankTransactionBeginningBalance.setBankAccount(bankAccount);
			}
			request.setBankTransactionBeginningBalanceId(0L);	
			BeanUtils.copyProperties(request,bankTransactionBeginningBalance);
			bankTransactionBeginningBalance.setCreateBy(request.getCurrentUser());
			bankTransactionBeginningBalanceRepository.save(bankTransactionBeginningBalance);
		}
		return bankTransactionBeginningBalance;
	}
	
	@Override
	public BankTransactionBeginningBalanceResponse getOne(Long bankTransactionBeginningBalanceId) throws Exception {
		BankTransactionBeginningBalance bankTransactionBeginningBalance = bankTransactionBeginningBalanceRepository.findById(bankTransactionBeginningBalanceId).orElseThrow(()->new ResourceNotFoundException("bankTransactionBeginningBalanceId "+bankTransactionBeginningBalanceId+" does not exist"));
		BankTransactionBeginningBalanceResponse response = BankTransactionBeginningBalanceResponse.builder().build();
		
		BeanUtils.copyProperties(bankTransactionBeginningBalance,response);

		if(bankTransactionBeginningBalance.getBankAccount()!=null) {
			BankAccount bankAccount = bankAccountRepository.findById(bankTransactionBeginningBalance.getBankAccount().getBankAccountId()).orElseThrow(()->new BadRequestException("bankAccountId"+bankTransactionBeginningBalance.getBankAccount().getBankAccountId()+" "+messageSource.getMessage("does.not.exist", null, LocaleContextHolder.getLocale())));
			response.setBankAccountId(bankAccount.getBankAccountId());
			response.setBankAccountNumber(bankAccount.getBankAccountNumber());
		}
		return response;
	}

	@Override
	public void removeOne(Long bankTransactionBeginningBalanceId) throws Exception {
		BankTransactionBeginningBalance bankTransactionBeginningBalance = bankTransactionBeginningBalanceRepository.findById(bankTransactionBeginningBalanceId).orElseThrow(()->new ResourceNotFoundException("bankTransactionBeginningBalanceId"+bankTransactionBeginningBalanceId+" does not exist"));
		bankTransactionBeginningBalanceRepository.delete(bankTransactionBeginningBalance);
	}

	@Override
	public PartialList<BankTransactionBeginningBalancePartialList> partialList(int pageNo, int pageSize, String sortField, 
			String sortDirection, Long bankAccountId, Date bankTransactionBeginningBalanceDate) throws Exception {
		
		Specification<BankTransactionBeginningBalancePartialList> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (Objects.nonNull(bankAccountId) && bankAccountId>0L) {
            	predicates.add(builder.equal(root.get("bankAccountId"), bankAccountId));
            }
            if (Objects.nonNull(bankTransactionBeginningBalanceDate)) {
            	predicates.add(builder.equal(root.get("bankTransactionBeginningBalanceDate"), bankTransactionBeginningBalanceDate));
            }
            
            if (StringUtils.hasText(sortField)&&!sortField.equals("companyName")&&!sortField.equals("officeName")&&!sortField.equals("bankAccount")&&!sortField.equals("bankTransactionBeginningBalanceAmount")&&!sortField.equals("bankTransactionBeginningBalanceDate")&&!sortField.equals("bankTransactionBeginningBalanceAmount")) {
            	if (sortDirection.equals("asc")) {
            		query.orderBy(builder.asc(root.get(sortField)));
                } else if (sortDirection.equals("desc")) {
                	query.orderBy(builder.desc(root.get(sortField)));
                }
    		} else {
    			query.orderBy(builder.desc(root.get("bankTransactionBeginningBalanceId")));
    		}
            
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
        
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<BankTransactionBeginningBalancePartialList> bankTransactionBeginningBalanceList = bankTransactionBeginningBalancePartialListRepository.findAll(specification, pageable);
		if(!bankTransactionBeginningBalanceList.getContent().isEmpty()) {
			
			for(BankTransactionBeginningBalancePartialList s : bankTransactionBeginningBalanceList) {
				BankAccount bankAccount = bankAccountRepository.findById(s.getBankAccountId()).orElseThrow(()->new BadRequestException("bankAccountId"+s.getBankAccountId()+" "+messageSource.getMessage("does.not.exist", null, LocaleContextHolder.getLocale())));
				s.setBankAccount(bankAccount.getBankAccountNumber());
			}
			
			if (StringUtils.hasText(sortField)&&sortField.equals("bankAccount")) {
				if (sortDirection.equals("asc")) {
					List<BankTransactionBeginningBalancePartialList> sortedList = new ArrayList<>(bankTransactionBeginningBalanceList.getContent());
				    Collections.sort(sortedList, Comparator.comparing(BankTransactionBeginningBalancePartialList::getBankAccount));
				    bankTransactionBeginningBalanceList = new PageImpl<>(sortedList, pageable, sortedList.size());			
				} else if (sortDirection.equals("desc")) {
					List<BankTransactionBeginningBalancePartialList> sortedList = new ArrayList<>(bankTransactionBeginningBalanceList.getContent());
				    Collections.sort(sortedList, Comparator.comparing(BankTransactionBeginningBalancePartialList::getBankAccount).reversed());
				    bankTransactionBeginningBalanceList = new PageImpl<>(sortedList, pageable, sortedList.size());
				}
			}
			
			if (StringUtils.hasText(sortField)&&sortField.equals("bankTransactionBeginningBalanceDate")) {
				if (sortDirection.equals("asc")) {
					List<BankTransactionBeginningBalancePartialList> sortedList = new ArrayList<>(bankTransactionBeginningBalanceList.getContent());
				    Collections.sort(sortedList, Comparator.comparing(BankTransactionBeginningBalancePartialList::getBankTransactionBeginningBalanceDate));
				    bankTransactionBeginningBalanceList = new PageImpl<>(sortedList, pageable, sortedList.size());			
				} else if (sortDirection.equals("desc")) {
					List<BankTransactionBeginningBalancePartialList> sortedList = new ArrayList<>(bankTransactionBeginningBalanceList.getContent());
				    Collections.sort(sortedList, Comparator.comparing(BankTransactionBeginningBalancePartialList::getBankTransactionBeginningBalanceDate).reversed());
				    bankTransactionBeginningBalanceList = new PageImpl<>(sortedList, pageable, sortedList.size());
				}
			}
			
			if (StringUtils.hasText(sortField)&&sortField.equals("bankTransactionBeginningBalanceAmount")) {
				if (sortDirection.equals("asc")) {
					List<BankTransactionBeginningBalancePartialList> sortedList = new ArrayList<>(bankTransactionBeginningBalanceList.getContent());
				    Collections.sort(sortedList, Comparator.comparing(BankTransactionBeginningBalancePartialList::getBankTransactionBeginningBalanceAmount));
				    bankTransactionBeginningBalanceList = new PageImpl<>(sortedList, pageable, sortedList.size());			
				} else if (sortDirection.equals("desc")) {
					List<BankTransactionBeginningBalancePartialList> sortedList = new ArrayList<>(bankTransactionBeginningBalanceList.getContent());
				    Collections.sort(sortedList, Comparator.comparing(BankTransactionBeginningBalancePartialList::getBankTransactionBeginningBalanceAmount).reversed());
				    bankTransactionBeginningBalanceList = new PageImpl<>(sortedList, pageable, sortedList.size());
				}
			}
		}
		return partialListUtil.createPartialList(bankTransactionBeginningBalanceList);
	}

}
