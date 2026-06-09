package com.example.demo.project.custom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.project.custom.entities.BankAccount;


@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> , JpaSpecificationExecutor<BankAccount>{

}
