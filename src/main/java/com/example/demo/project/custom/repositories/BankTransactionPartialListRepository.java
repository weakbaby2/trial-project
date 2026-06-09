package com.example.demo.project.custom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.project.custom.entities.BankTransactionPartialList;


@Repository
public interface BankTransactionPartialListRepository extends JpaRepository<BankTransactionPartialList,Long>, JpaSpecificationExecutor<BankTransactionPartialList> {

}
