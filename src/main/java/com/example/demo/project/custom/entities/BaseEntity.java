package com.example.demo.project.custom.entities;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter @Setter
@Audited
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@CreatedBy
    @Column(length = 128, name = "create_by",insertable = true,updatable = false)
    private String createBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_on",insertable = true,updatable = false)
    private Date createOn;

    @LastModifiedBy
    @Column(length = 128, name = "change_by")
    private String changeBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "change_on")
    private Date changeOn;
    
    @PrePersist
	void onCreate() {
    	this.setChangeBy(null);
    	this.setChangeOn(null);
	}
    
}
