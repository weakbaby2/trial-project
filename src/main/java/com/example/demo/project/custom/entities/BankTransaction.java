package com.example.demo.project.custom.entities;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="bank_transaction")
@BatchSize(size=100)
@AuditTable(schema="audit",value="bank_transaction_aud")
@Audited
@Getter 
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankTransaction extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(allocationSize=1,initialValue=1,name="bank_transaction_seq",sequenceName="bank_transaction_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="bank_transaction_seq")
	@Column(name="bank_transaction_id", nullable=false)
	private long bankTransactionId;
	
	@OneToOne
	@JoinColumn(name="bank_account_id")
	@Fetch(FetchMode.JOIN)
	BankAccount bankAccount;
	
	@Temporal(TemporalType.DATE)
	@Column(name="bank_transaction_date")
	private Date bankTransactionDate;

	@Column(name = "amount")
    private BigDecimal amount;
	
	@Column(name="note",length=255)
	String note;
}
