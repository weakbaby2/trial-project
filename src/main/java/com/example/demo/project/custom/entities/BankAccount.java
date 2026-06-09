package com.example.demo.project.custom.entities;

import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="bank_account")
@BatchSize(size=100)
@AuditTable(schema="audit",value="bank_account_aud")
@Audited
@Getter 
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(allocationSize=1,initialValue=1,name="bank_account_seq",sequenceName="bank_account_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="bank_account_seq")
	@Column(name="bank_account_id", nullable=false)
	private long bankAccountId;
	
	@Column(name="bank_account_name",length=128)
	String bankAccountName;

	@Column(name="bank_account_number",length=64)
	String bankAccountNumber;
	
}
